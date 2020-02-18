/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import org.apache.thrift.TException;

import com.vng.zing.resource.thrift.TI32Result;
import com.vng.zing.resource.thrift.TUserResult;
import com.vng.zing.resource.thrift.TWriteService;
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.model.TWriteServiceModel;

/**
 *
 * @author namnh16
 */
public class TWriteServiceHandler implements TWriteService.Iface {

    @Override
    public TUserResult upgrade(User user) throws TException {
        //        ThreadProfiler profiler = Profiler.createThreadProfiler("TAppHandler.upgrade", false);

        try {
            return TWriteServiceModel.INSTANCE.upgrade(user);
//        } catch (ZExceptionHandler ex) {
//            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }

    @Override
    public TI32Result add(Token token, User user) throws TException {
        //        ThreadProfiler profiler = Profiler.createThreadProfiler("TAccountHandler.add", false);

        try {
            return TWriteServiceModel.INSTANCE.add(token, user);
//        } catch (ZExceptionHandler ex) {
//            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }

    @Override
    public TI32Result remove(int uId) throws TException {
        //        ThreadProfiler profiler = Profiler.createThreadProfiler("TAccountHandler.add", false);

        try {
            return TWriteServiceModel.INSTANCE.remove(uId);
//        } catch (ZExceptionHandler ex) {
//            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }

}
