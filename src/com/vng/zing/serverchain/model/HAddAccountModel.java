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
import com.vng.zing.thrift.client.TWriteServiceClient;
import com.vng.zing.thrift.resource.TI32Result;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thrift.resource.Token;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.thrift.resource.UserType;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HAddAccountModel extends HBaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HAddAccountModel.class);
    public static final HAddAccountModel INSTANCE = new HAddAccountModel();

    private HAddAccountModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "HAddAccountModel");

        this.prepareHeaderHtml(response);

        try {
            String username = ServletUtils.getString(request, "username");
            String encrPassword = Utils.md5(ServletUtils.getString(request, "password"));
            String name = ServletUtils.getString(request, "name");

            TReadServiceClient readClient = TClientPoolManager.getReadServiceClient();
            TUserResult queryResult = readClient.findByUsername(username);

            if (queryResult.getError() == ECode.C_SUCCESS.getValue()) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.ALREADY_EXIST));
            } else {
                Token token = new Token();
                token.setUsername(username);
                token.setPassword(encrPassword);

                User user = new User();
                user.setUsername(username);
                user.setName(name);
                user.setType(UserType.REGULAR);
                user.setJoinDate(Utils.getCurrentSQLDate().toString());

                TWriteServiceClient writeClient = TClientPoolManager.getWriteServiceClient();
                TI32Result updateResult = writeClient.add(token, user);
                if (updateResult.getError() != ECode.C_SUCCESS.getValue()) {
                    this.outAndClose(request, response, MessageGenerator.getMessage(updateResult.getError()));
                } else {
                    response.sendRedirect("/");
                }
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
            profiler.pop(this.getClass(), "HAddAccountModel");
        }
    }
}
