/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Application;
import com.vng.zing.resource.thrift.DatabaseException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.model.TAppModel;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author namnh16
 */
public class TApplicationHandler implements Application.Iface{
    private static final Logger _Logger = ZLogger.getLogger(TApplicationHandler.class);
    
    @Override
    public User upgrade(User user) throws DatabaseException, TException{
        ThreadProfiler profiler = Profiler.createThreadProfiler("TAppHandler.upgrade", false);
        
        try{
            return TAppModel.INSTANCE.upgrade(user);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
    
    @Override
    public String showInfo(User user) throws DatabaseException, TException{
        ThreadProfiler profiler = Profiler.createThreadProfiler("TAppHandler.remove", false);
        
        try{
            return TAppModel.INSTANCE.showInfo(user);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
}
