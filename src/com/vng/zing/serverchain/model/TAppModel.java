/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.DatabaseException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.utils.Utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author namnh16
 */
public class TAppModel {
    private static final Logger _Logger = ZLogger.getLogger(TAppModel.class);
    public static final TAppModel INSTANCE = new TAppModel();
    
    private TAppModel(){
        
    }
    
    public User upgrade(User user) throws DatabaseException, TException {
        if(user.getFieldValue(user.fieldForId(3)) == UserType.findByValue(0)){  // 0: REGULAR
            user.setFieldValue(user.fieldForId(3), UserType.findByValue(1));    // 1: PREMIUM
            
            try(
            Connection connection = Utils.getAppDBConnection();
            PreparedStatement stm = connection.prepareStatement("UPDATE User "
                    + "SET type='PREMIUM' where id=?")
            ){
                stm.setInt(1, (int) user.getFieldValue(user.fieldForId(1)));
            
                stm.executeUpdate();
            }
            catch(SQLException sqle){
                _Logger.error(sqle);
            }
        }
        else{
            throw new DatabaseException("Unable to upgrade further");
        }
        
        return user;
    }
    
    public String showInfo(User user) throws DatabaseException, TException {
        StringBuilder info = new StringBuilder();
        
        // REGULAR user can't see their internal ID. This is a heavily
        // simplified way of showing access control
        if(user.getFieldValue(user.fieldForId(3)) != UserType.findByValue(0)){
            info.append("ID: ");
            info.append(user.getFieldValue(user.fieldForId(1)).toString());     // 1: id
            info.append("\n");
        }
        
        info.append("Name: ");
        info.append((String) user.getFieldValue(user.fieldForId(2)));           // 2: name
        info.append("\n");
        
        info.append("User type: ");
        info.append(user.getFieldValue(user.fieldForId(3)).toString());         // 3: type
        info.append("\n");
        
        info.append("Join date: ");
        info.append(user.getFieldValue(user.fieldForId(4)).toString());         // 4: joinDate
        info.append("\n");
        
        return info.toString();
    }
}
