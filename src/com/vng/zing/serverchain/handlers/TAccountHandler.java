/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.engine.sql.exception.ZException;
import com.vng.zing.resource.thrift.Account;
import com.vng.zing.resource.thrift.TZException;
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.model.TAccountModel;

/**
 *
 * @author namnh16
 */
public class TAccountHandler implements Account.Iface {
//    private static final Logger _Logger = ZLogger.getLogger(TAccountHandler.class);

    @Override
    public void add(Token token, User user) throws TZException {
//        ThreadProfiler profiler = Profiler.createThreadProfiler("TAccountHandler.add", false);

        try {
            TAccountModel.INSTANCE.add(token, user);
        } catch (ZException ex) {
            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }

    @Override
    public void remove(int uId) throws TZException {
//        ThreadProfiler profiler = Profiler.createThreadProfiler("TAccountHandler.add", false);

        try {
            TAccountModel.INSTANCE.remove(uId);
        } catch (ZException ex) {
            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}
