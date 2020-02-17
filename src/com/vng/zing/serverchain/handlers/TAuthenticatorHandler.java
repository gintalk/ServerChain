/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.engine.sql.exception.ZExceptionHandler;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.resource.thrift.TZException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.model.TAuthenticatorModel;

/**
 *
 * @author namnh16
 */
public class TAuthenticatorHandler implements Authenticator.Iface {
//    private static final Logger _Logger = ZLogger.getLogger(TAuthenticatorHandler.class);

    @Override
    public User authenticate(String username, String password) throws TZException {
//        ThreadProfiler profiler = Profiler.createThreadProfiler("TAuthenticatorHandler.authenticate", false);

        try {
            return TAuthenticatorModel.INSTANCE.authenticate(username, password);
//        } catch (ZExceptionHandler ex) {
//            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}
