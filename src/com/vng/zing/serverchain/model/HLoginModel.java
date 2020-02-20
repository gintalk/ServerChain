/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.media.common.utils.ServletUtils;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thrift.client.TClientPoolManager;
import com.vng.zing.thrift.client.TReadServiceClient;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HLoginModel extends HBaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HLoginModel.class);
    public static final HLoginModel INSTANCE = new HLoginModel();

    private HLoginModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "HLoginModel");

        this.prepareHeaderHtml(response);

        try {
            String username = ServletUtils.getString(request, "username");
            String encrPassword = Utils.md5(ServletUtils.getString(request, "password"));

            TReadServiceClient readClient = TClientPoolManager.getReadServiceClient();
            TUserResult queryResult = readClient.authenticate(username, encrPassword);

            if (queryResult.getError() == ECode.C_SUCCESS.getValue()) {
                request.getSession(true).setAttribute("user", queryResult.getValue());
                response.sendRedirect("/user/info");
            } else {
                this.outAndClose(request, response, MessageGenerator.getMessage(queryResult.getError()));
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
            profiler.pop(this.getClass(), "HLoginModel");
        }
    }
}
