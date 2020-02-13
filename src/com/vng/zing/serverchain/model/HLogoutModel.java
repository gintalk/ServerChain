/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;

/**
 *
 * @author namnh16
 */
public class HLogoutModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HLogoutModel.class);
    public static final HLogoutModel INSTANCE = new HLogoutModel();
//    private  static final String SERVICE_NAME = "Authenticator";

    private HLogoutModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

        try {
            request.getSession(false).invalidate();
            response.sendRedirect("/");

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}
