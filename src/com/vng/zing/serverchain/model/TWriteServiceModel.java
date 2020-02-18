/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.sql.Date;

import org.apache.log4j.Logger;

import com.vng.zing.engine.dal.TokenDal;
import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.engine.type.KVPair;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.TI32Result;
import com.vng.zing.resource.thrift.TUserResult;
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class TWriteServiceModel {

    private static final Logger LOGGER = ZLogger.getLogger(TWriteServiceModel.class);
    public static final TWriteServiceModel INSTANCE = new TWriteServiceModel();

    private TWriteServiceModel() {

    }

    public TUserResult upgrade(User user) {
        TUserResult result = new TUserResult();

        UserType type = user.getType();
        if (type == UserType.REGULAR) {
            TI32Result updateResult = UserDal.INSTANCE.updateItem(user.getId(), new KVPair("type", UserType.PREMIUM.toString()));
            if (updateResult.getError() == ECode.C_SUCCESS.getValue()) {
                user.setType(UserType.PREMIUM);

                result.setError(ECode.C_SUCCESS.getValue());
            } else {
                result.setError(ECode.C_FAIL.getValue());
            }
        } else {
            result.setError(ECode.REACH_MAX.getValue());
        }
        result.setValue(user);

        return result;
    }

    public TI32Result add(Token token, User user) {
        TI32Result result = new TI32Result();

        String username = (String) token.getUsername();
        String password = Utils.md5((String) token.getPassword());

        TI32Result updateTokenResult = TokenDal.INSTANCE.addItemAutoKey(username, password);
        if (updateTokenResult.getError() == ECode.C_SUCCESS.getValue()) {
            String name = (String) user.getFieldValue(user.fieldForId(2));
            String type = Utils.toString(UserType.REGULAR);
            Date joinDate = Utils.getCurrentSQLDate();

            TI32Result updateUserResult = UserDal.INSTANCE.addItem(updateTokenResult.getValue(), name, type, joinDate);
            if (updateUserResult.getError() == ECode.C_SUCCESS.getValue()) {
                result.setError(ECode.C_SUCCESS.getValue());
            } else {
                TokenDal.INSTANCE.removeItem(updateTokenResult.getValue());

                result.setError(ECode.C_FAIL.getValue());
            }
        } else {
            result.setError(ECode.C_FAIL.getValue());
        }

        return result;
    }

    public TI32Result remove(int id) {
        TI32Result result = new TI32Result();

        TI32Result updateResult = TokenDal.INSTANCE.removeItem(id);
        if (updateResult.getError() == ECode.C_SUCCESS.getValue()) {
            result.setError(ECode.C_SUCCESS.getValue());
        } else if (updateResult.getError() == ECode.UNCHANGED.getValue()) {
            result.setError(ECode.NOT_EXIST.getValue());
        } else {
            result.setError(ECode.C_FAIL.getValue());
        }

        return result;
    }
}
