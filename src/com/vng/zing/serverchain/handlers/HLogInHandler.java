/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.serverchain.model.HLogInModel;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author namnh16
 */
public class HLogInHandler extends HttpServlet{
    private static final Logger _Logger = ZLogger.getLogger(HLogInHandler.class);
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        this.doProcess(request, response);
    }
    
    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ThreadProfiler profiler = Profiler.createThreadProfilerInHttpProc("LogInHandler", request);
        
        try{
            HLogInModel.INSTANCE.process(request, response);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
}
