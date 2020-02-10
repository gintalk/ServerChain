/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.serverchain.model.HShowInfoModel;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author namnh16
 */
public class HShowInfoHandler extends HttpServlet{
    private static final Logger _Logger = ZLogger.getLogger(HShowInfoHandler.class);
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        this.doProcess(request, response);
    }
    
    private void doProcess(HttpServletRequest request, HttpServletResponse response){
        ThreadProfiler profiler = Profiler.createThreadProfilerInHttpProc("ShowInfoHandler", request);
        
        try{
            HShowInfoModel.INSTANCE.process(request, response);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
}
