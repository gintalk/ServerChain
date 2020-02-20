/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.rythmengine.Rythm;
import org.rythmengine.utils.NamedParams;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HShowInfoModel extends HBaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HShowInfoModel.class);
    public static final HShowInfoModel INSTANCE = new HShowInfoModel();

    private HShowInfoModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "HShowInfoModel");

        this.prepareHeaderHtml(response);

        try {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user == null) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.UNLOADED));
            } else {
                this.outAndClose(request, response, getInfoString(user));
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
            profiler.pop(this.getClass(), "HShowInfoModel");
        }
    }

    private String getInfoString(User user) throws IOException {
        String id = String.valueOf(user.getId());
        String name = user.getName();
        String type = user.getType().toString();
        String joinDate = user.getJoinDate();
        String privilege = ("ADMIN".equals(type)) ? "Abuse your power!" : "Upgrade";
        String privilegePath = ("ADMIN".equals(type)) ? "/template/rythm/layout/remove.html" : "/user/upgrade";
        String profilePic
            = ("Tom".equals(name) || "Spike".equals(name) || "Jerry".equals(name) || "Toodles".equals(name))
            ? name + ".jpg" : "EricCartman.jpg";

        return Rythm.render("info.rythm", NamedParams.from(
            NamedParams.p("id", id),
            NamedParams.p("name", name),
            NamedParams.p("type", type),
            NamedParams.p("joinDate", joinDate),
            NamedParams.p("privilege", privilege),
            NamedParams.p("privilegePath", privilegePath),
            NamedParams.p("profilePic", profilePic)));
    }
}
