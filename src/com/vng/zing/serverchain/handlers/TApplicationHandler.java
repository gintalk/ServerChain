/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.engine.sql.exception.ZException;
import com.vng.zing.resource.thrift.Application;
import com.vng.zing.resource.thrift.TZException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.model.TApplicationModel;

/**
 *
 * @author namnh16
 */
public class TApplicationHandler implements Application.Iface {
//    private static final Logger _Logger = ZLogger.getLogger(TApplicationHandler.class);

    @Override
    public User upgrade(User user) throws TZException {
//        ThreadProfiler profiler = Profiler.createThreadProfiler("TAppHandler.upgrade", false);

        try {
            return TApplicationModel.INSTANCE.upgrade(user);
        } catch (ZException ex) {
            throw new TZException(ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }

//    @Override
//    public String showInfo(User user) throws DatabaseException, TException{
////        ThreadProfiler profiler = Profiler.createThreadProfiler("TAppHandler.remove", false);
//        
//        try{
//            return TApplicationModel.INSTANCE.showInfo(user);
//        }
//        finally{
////            Profiler.closeThreadProfiler();
//        }
//    }
}
