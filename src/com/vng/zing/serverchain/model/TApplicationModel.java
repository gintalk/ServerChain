/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import org.apache.log4j.Logger;

import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.engine.sql.exception.ZExceptionHandler;
import com.vng.zing.engine.type.Pair;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.TZException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.utils.Utils;

/**
 *
 * @author namnh16
 */
public class TApplicationModel {

    private static final Logger LOGGER = ZLogger.getLogger(TApplicationModel.class);
    public static final TApplicationModel INSTANCE = new TApplicationModel();

    private TApplicationModel() {

    }

    public User upgrade(User user) throws TZException {
        if (user == null) {
            return user;
        }

        UserType type = (UserType) user.getFieldValue(user.fieldForId(3));
        if (type == UserType.findByValue(0)) {
            boolean success = UserDal.INSTANCE.updateItem(
                (int) user.getFieldValue(user.fieldForId(1)),
                new Pair("type", Utils.toString(UserType.findByValue(1)))
            );

            if (success) {
                user.setFieldValue(user.fieldForId(3), UserType.findByValue(1));
            }
        } else {
            TZException tzex = new TZException();
            ZExceptionHandler.INSTANCE.prepareException(
                tzex,
                ZExceptionHandler.State.MAXIMUM_PRIVILEGE
            );
            throw tzex;
//            throw new ZExceptionHandler("Unable to upgrade further!", ZExceptionHandler.State.MAXIMUM_PRIVILEGE);
        }

        return user;
    }
}
