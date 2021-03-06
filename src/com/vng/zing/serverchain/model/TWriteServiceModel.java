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
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thrift.resource.TI32Result;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thrift.resource.Token;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.thrift.resource.UserType;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class TWriteServiceModel extends TBaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(TWriteServiceModel.class);
    public static final TWriteServiceModel INSTANCE = new TWriteServiceModel();

    private TWriteServiceModel() {

    }

    public TUserResult upgrade(User user) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "TWriteServiceModel.upgrade");

        TUserResult result = new TUserResult();
        UserType type = user.getType();
        if (type == UserType.REGULAR) {
            TI32Result updateResult = UserDal.INSTANCE.updateItem(user.getId(), new KVPair<>("type", UserType.PREMIUM.toString()));
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
        if (result.getError() == ECode.C_SUCCESS.getValue()) {
            updateRelevantCache(user.getId(), result);
        }

        profiler.pop(this.getClass(), "TWriteServiceModel.upgrade");
        return result;
    }

    public TI32Result add(Token token, User user) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "TWriteServiceModel.add");

        TI32Result result = new TI32Result();

        String username = (String) token.getUsername();
        String password = (String) token.getPassword();

        TI32Result updateTokenResult = TokenDal.INSTANCE.addItemAutoKey(username, password);
        if (updateTokenResult.getError() == ECode.C_SUCCESS.getValue()) {

            TI32Result updateUserResult = UserDal.INSTANCE.addItem(
                updateTokenResult.getValue(),
                user.getUsername(),
                user.getName(),
                user.getType().toString(),
                Date.valueOf(user.getJoinDate())
            );
            if (updateUserResult.getError() == ECode.C_SUCCESS.getValue()) {
                result.setError(ECode.C_SUCCESS.getValue());
                clearRelevantCache(token.getUsername());
            } else {
                TokenDal.INSTANCE.removeItem(updateTokenResult.getValue());

                result.setError(ECode.C_FAIL.getValue());
            }
        } else {
            result.setError(ECode.C_FAIL.getValue());
        }

        profiler.pop(this.getClass(), "TWriteServiceModel.add");
        return result;
    }

    public TI32Result remove(int uId) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "TWriteServiceModel.remove");

        TI32Result result = new TI32Result();

        TI32Result updateResult = TokenDal.INSTANCE.removeItem(uId);
        if (updateResult.getError() == ECode.C_SUCCESS.getValue()) {
            result.setError(ECode.C_SUCCESS.getValue());
            clearRelevantCache(uId);
        } else if (updateResult.getError() == ECode.UNCHANGED.getValue()) {
            result.setError(ECode.NOT_EXIST.getValue());
        } else {
            result.setError(ECode.C_FAIL.getValue());
        }

        profiler.pop(this.getClass(), "TWriteServiceModel.remove");
        return result;
    }
}
