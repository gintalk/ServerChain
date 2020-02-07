/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.resource.thrift.DatabaseException;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.model.TAuthenticatorModel;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
/**
 *
 * @author namnh16
 */
public class TAuthenticatorHandler implements Authenticator.Iface{
    private static final Logger _Logger = ZLogger.getLogger(TAuthenticatorHandler.class);
    
    @Override
    public User authenticate(String username, String password)
            throws InvalidTokenException, DatabaseException, TException{
        ThreadProfiler profiler = Profiler.createThreadProfiler("TAuthenticatorHandler.authenticate", false);
        
        try{
            return TAuthenticatorModel.INSTANCE.authenticate(username, password);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
}
