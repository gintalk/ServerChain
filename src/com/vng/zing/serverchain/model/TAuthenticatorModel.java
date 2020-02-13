/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.engine.dal.UserTokenDal;
import com.vng.zing.engine.sql.exception.ZException;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.utils.Utils;

/**
 *
 * @author namnh16
 */
public class TAuthenticatorModel {

    private static final Logger LOGGER = ZLogger.getLogger(TAuthenticatorModel.class);
    public static final TAuthenticatorModel INSTANCE = new TAuthenticatorModel();

    private TAuthenticatorModel() {

    }

    public User authenticate(String username, String password) throws ZException {
        HashMap<String, Object> tokenMap = UserTokenDal.INSTANCE.getItemAsMap(username);
        if (tokenMap == null
            || !Utils.md5(password).equals(tokenMap.get("password"))) {
            throw new ZException("Incorrect username or password", ZException.State.INVALID_TOKEN);
        }

        HashMap<String, Object> userMap = UserDal.INSTANCE.getItemAsMap((int) tokenMap.get("id"));
        if (userMap == null) {
            throw new ZException("User does not exist", ZException.State.MISSING_USER);
        }

        return Utils.mapToUser(userMap);
    }

//    public User authenticate(String username, String password)
//            throws InvalidTokenException, TException {
//        Token token = new Token();
//
//        token.setFieldValue(token.fieldForId(1), username);
//        token.setFieldValue(token.fieldForId(2), password);
//
//        return authenticateByToken(token);
//    }
//
//    private User authenticateByToken(Token token)
//            throws InvalidTokenException, DatabaseException, TException {
//        try (
//                Connection connection = Utils.getAuthDBConnection();
//                PreparedStatement stm = connection.prepareStatement("SELECT "
//                        + "id, username, password FROM UserToken WHERE username=?")) {
//            String username = (String) token.getFieldValue(token.fieldForId(1));
//            String password = Utils.md5((String) token.getFieldValue(token.fieldForId(2)));
//
//            stm.setString(1, username);
//
//            try (ResultSet resultSet = stm.executeQuery()) {
//                if (!resultSet.isBeforeFirst()) {
//                    throw new InvalidTokenException("User " + username + " does not exist!");
//                }
//                resultSet.next();
//
//                if (!resultSet.getString("password").equals(password)) {
//                    throw new InvalidTokenException("Incorrect password!");
//                }
//
//                return Utils.findUserById(resultSet.getInt("id"));
//            }
//        } catch (SQLException sqle) {
//            throw new DatabaseException(sqle.toString());
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
//            throw new TException(ex.getMessage());
//        }
//    }
}
