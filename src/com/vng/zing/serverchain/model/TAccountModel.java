/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.sql.Date;

import org.apache.log4j.Logger;

import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.engine.dal.UserTokenDal;
import com.vng.zing.engine.sql.exception.ZException;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.utils.Utils;

/**
 *
 * @author namnh16
 */
public class TAccountModel {

    private static final Logger LOGGER = ZLogger.getLogger(TAccountModel.class);
    public static final TAccountModel INSTANCE = new TAccountModel();

    private TAccountModel() {

    }

    public void add(Token token, User user) throws ZException {
        if (token == null || user == null) {
            return;
        }

        String username = (String) token.getFieldValue(token.fieldForId(1));
        String password = Utils.md5((String) token.getFieldValue(token.fieldForId(2)));

        int tokenAutoKey = UserTokenDal.INSTANCE.addItemAutoKey(username, password);
        if (tokenAutoKey > 0) {
            String name = (String) user.getFieldValue(user.fieldForId(2));
            String type = Utils.toString(UserType.REGULAR);
            Date joinDate = Utils.getCurrentSQLDate();

            if (!UserDal.INSTANCE.addItem(tokenAutoKey, name, type, joinDate)) {
                UserTokenDal.INSTANCE.removeItem(tokenAutoKey);
                throw new ZException("Add user failed", ZException.State.ADD_USER_FAILED);
            }
        } else {
            throw new ZException("Add token failed", ZException.State.ADD_TOKEN_FAILED);
        }
    }

    public void remove(int id) throws ZException {
        if (id < 1) {
            return;
        }

        if (!UserTokenDal.INSTANCE.removeItem(id)) {
            throw new ZException("Remove token failed", ZException.State.REMOVE_TOKEN_FAILED);
        }
    }

}
