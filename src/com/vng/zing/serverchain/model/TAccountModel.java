/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author namnh16
 */
public class TAccountModel {
    private static final Logger _Logger = ZLogger.getLogger(TAccountModel.class);
    public static final TAccountModel INSTANCE = new TAccountModel();
    
    private TAccountModel(){
        
    }
    
    public void add(Token token, User user) throws InvalidTokenException, TException{
        int curAI = -1;
        
        try(
            Connection authCon = Utils.getAuthDBConnection();
            PreparedStatement authStm = authCon.prepareStatement("INSERT INTO "
                    + "UserToken VALUES(?, ?, ?)");
            PreparedStatement curStm = authCon.prepareStatement("SELECT "
                + "AUTO_INCREMENT FROM information_schema.TABLES WHERE "
                + "table_name='UserToken' AND table_schema=database()");
            ResultSet resultSet = curStm.executeQuery();
        ){
            resultSet.next();
            curAI = resultSet.getInt("AUTO_INCREMENT");
            
            authStm.setInt(1, curAI);
            authStm.setString(2, (String) token.getFieldValue(token.fieldForId(1)));                    // 1: username
            authStm.setString(3, Utils.md5((String) token.getFieldValue(token.fieldForId(2))));         // 2: password
            
            authStm.executeUpdate();
        }
        catch(SQLException sqle){
            if(sqle.getErrorCode() == 1062){
                throw new InvalidTokenException("User " +
                    (String) token.getFieldValue(token.fieldForId(1)) + " already exists");
            }
            else{
                _Logger.error(sqle);
            }
        }
        catch(NoSuchAlgorithmException|UnsupportedEncodingException ex){
            _Logger.error(ex);
        }
        
        try(
            Connection appCon = Utils.getAppDBConnection();
                
            PreparedStatement appStm = appCon.prepareStatement("INSERT INTO "
                    + "User VALUES(?, ?, ?, ?)");
        ){
            appStm.setInt(1, curAI);                                                                            // 1: id
            appStm.setString(2, (String) user.getFieldValue(user.fieldForId(2)));                               // 2: name
            appStm.setString(3, Utils.toStringFromUserType(UserType.REGULAR));                                  // 3: type
            appStm.setDate(4, Utils.getCurrentSQLDate());                                                       // 4: joinDate
            
            appStm.executeUpdate();
        }
        catch(SQLException sqle){
            _Logger.error(sqle);
        }
    }
}
