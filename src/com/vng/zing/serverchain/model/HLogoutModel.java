/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HLogoutModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HLogoutModel.class);
    public static final HLogoutModel INSTANCE = new HLogoutModel();

    private HLogoutModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

        try {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user == null) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.UNLOADED));
            } else {
                request.getSession(false).invalidate();
                response.sendRedirect("/");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));

        } finally {
            //            Profiler.closeThreadProfiler();
        }
    }
}
