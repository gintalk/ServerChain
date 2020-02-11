/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vng.zing.serverchain.model.HLogInModel;

/**
 *
 * @author namnh16
 */
public class HLoginHandler extends HttpServlet {
//    private static final Logger _Logger = ZLogger.getLogger(HLoginHandler.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        this.doProcess(request, response);
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.createThreadProfilerInHttpProc("LogInHandler", request);

        try {
            HLogInModel.INSTANCE.process(request, response);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}
