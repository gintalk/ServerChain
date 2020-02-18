/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import org.apache.thrift.TException;

import com.vng.zing.resource.thrift.TReadService;
import com.vng.zing.resource.thrift.TUserResult;
import com.vng.zing.serverchain.model.TReadServiceModel;

/**
 *
 * @author namnh16
 */
public class TReadServiceHandler implements TReadService.Iface {

    @Override
    public TUserResult authenticate(String username, String password) throws TException {
        //        ThreadProfiler profiler = Profiler.createThreadProfiler("TAuthenticatorHandler.authenticate", false);

        try {
            return TReadServiceModel.INSTANCE.authenticate(username, password);
//        } catch (ZExceptionHandler ex) {
//            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }

}
