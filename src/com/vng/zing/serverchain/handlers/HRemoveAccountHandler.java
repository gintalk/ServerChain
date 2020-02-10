/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vng.zing.serverchain.model.HRemoveAccountModel;

/**
 *
 * @author namnh16
 */
public class HRemoveAccountHandler extends HttpServlet{
//    private static final Logger _Logger = ZLogger.getLogger(HRemoveAccountHandler.class);
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        this.doProcess(request, response);
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.createThreadProfilerInHttpProc("RemoveAccountHandler", request);
        
        try{
            HRemoveAccountModel.INSTANCE.process(request, response);
        }
        finally{
//            Profiler.closeThreadProfiler();
        }
    }
}
