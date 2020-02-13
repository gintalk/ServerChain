/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import org.apache.log4j.Logger;

import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.engine.sql.exception.ZException;
import com.vng.zing.engine.type.Pair;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;

/**
 *
 * @author namnh16
 */
public class TApplicationModel {

    private static final Logger LOGGER = ZLogger.getLogger(TApplicationModel.class);
    public static final TApplicationModel INSTANCE = new TApplicationModel();

    private TApplicationModel() {

    }

    public User upgrade(User user) throws ZException {
        if (user == null) {
            return user;
        }

        UserType type = (UserType) user.getFieldValue(user.fieldForId(3));
        if (type == UserType.findByValue(0)) {
            boolean success = UserDal.INSTANCE.updateItem(
                (int) user.getFieldValue(user.fieldForId(1)),
                new Pair("type", "PREMIUM")
            );

            if (success) {
                user.setFieldValue(user.fieldForId(3), UserType.findByValue(1));
            }
        } else {
            throw new ZException("Unable to upgrade further!", ZException.State.MAXIMUM_PRIVILEGE);
        }

        return user;
    }
}
