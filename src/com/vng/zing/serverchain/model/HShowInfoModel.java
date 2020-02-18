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
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.cache.IntStringCache;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HShowInfoModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HShowInfoModel.class);
    public static final HShowInfoModel INSTANCE = new HShowInfoModel();

    private HShowInfoModel() {

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
                String cacheResult = IntStringCache.INSTANCE.get(user.getId());
                if (cacheResult == null) {
                    cacheResult = this.getInfoString(user);
                    IntStringCache.INSTANCE.put(user.getId(), cacheResult);
                }
                this.outAndClose(request, response, cacheResult);
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }

    private String getInfoString(User user) throws IOException {
        String id = String.valueOf(user.getFieldValue(user.fieldForId(1)));
        String name = (String) user.getFieldValue(user.fieldForId(2));
        String type = Utils.toString((UserType) user.getFieldValue(user.fieldForId(3)));
        String joinDate = (String) user.getFieldValue(user.fieldForId(4));
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
