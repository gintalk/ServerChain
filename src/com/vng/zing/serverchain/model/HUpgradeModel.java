/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thrift.client.TClientPoolManager;
import com.vng.zing.thrift.client.TWriteServiceClient;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HUpgradeModel extends HBaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HUpgradeModel.class);
    public static final HUpgradeModel INSTANCE = new HUpgradeModel();

    private HUpgradeModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "HUpgradeModel");

        this.prepareHeaderHtml(response);

        try {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user == null) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.UNLOADED));
            } else {
                TWriteServiceClient writeClient = TClientPoolManager.getWriteServiceClient();
                TUserResult updateResult = writeClient.upgrade(user);

                if (updateResult.getError() == ECode.C_SUCCESS.getValue()) {
                    request.getSession(false).setAttribute("user", updateResult.getValue());
                    response.sendRedirect("/user/info");
                } else {
                    this.outAndClose(request, response, MessageGenerator.getMessage(updateResult.getError()));
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
            profiler.pop(this.getClass(), "HUpgradeModel");
        }
    }
}
