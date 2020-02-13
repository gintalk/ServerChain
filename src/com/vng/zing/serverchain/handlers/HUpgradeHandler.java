/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.handlers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vng.zing.serverchain.model.HUpgradeModel;

/**
 *
 * @author namnh16
 */
public class HUpgradeHandler extends HttpServlet {
//    private static final Logger _Logger = ZLogger.getLogger(HUpgradeHandler.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
//        ThreadProfiler profiler = Profiler.createThreadProfilerInHttpProc("UpgradeHandler", request);

        try {
            HUpgradeModel.INSTANCE.process(request, response);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}
