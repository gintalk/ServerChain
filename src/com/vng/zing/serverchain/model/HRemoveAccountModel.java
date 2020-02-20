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
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thrift.client.TClientPoolManager;
import com.vng.zing.thrift.client.TReadServiceClient;
import com.vng.zing.thrift.client.TWriteServiceClient;
import com.vng.zing.thrift.resource.TI32Result;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.thrift.resource.UserType;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HRemoveAccountModel extends HBaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HRemoveAccountModel.class);
    public static final HRemoveAccountModel INSTANCE = new HRemoveAccountModel();

    private HRemoveAccountModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "HRemoveAccountModel");

        this.prepareHeaderHtml(response);

        try {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user == null) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.UNLOADED));
            } else if (user.getType() != UserType.ADMIN) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.NOT_ALLOW));
            } else {
                int uId = ServletUtils.getInt(request, "id", 0);

                TReadServiceClient readClient = TClientPoolManager.getReadServiceClient();
                TUserResult userResult = readClient.findById(uId);

                if (userResult.getError() == ECode.C_SUCCESS.getValue()) {
                    TWriteServiceClient writeClient = TClientPoolManager.getWriteServiceClient();
                    TI32Result updateResult = writeClient.remove(uId);

                    if (updateResult.getError() != ECode.C_SUCCESS.getValue()) {
                        this.outAndClose(request, response, MessageGenerator.getMessage(updateResult.getError()));
                    } else {
                        response.sendRedirect("/user/info");
                    }
                } else {
                    this.outAndClose(request, response, MessageGenerator.getMessage(userResult.getError()));
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
            profiler.pop(this.getClass(), "HRemoveAccountModel");
        }
    }
}
