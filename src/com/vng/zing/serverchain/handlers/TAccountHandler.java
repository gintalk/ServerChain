/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Account;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.model.TAccountModel;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author namnh16
 */
public class TAccountHandler implements Account.Iface{
    private static final Logger _Logger = ZLogger.getLogger(TAccountHandler.class);
    
    @Override
    public void add(Token token, User user) throws InvalidTokenException, TException{
        ThreadProfiler profiler = Profiler.createThreadProfiler("TAccountHandler.add", false);
        
        try{
            TAccountModel.INSTANCE.add(token, user);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
    
    @Override
    public void remove(int uId) throws InvalidTokenException, TException{
        ThreadProfiler profiler = Profiler.createThreadProfiler("TAccountHandler.add", false);
        
        try{
            TAccountModel.INSTANCE.remove(uId);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
}
