/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.thriftpool.TClientPoolConfig;
import com.vng.zing.thriftserver.ThriftServers;

/**
 * @Note: Class base model xử lý business chung & hàm tiện ích cho tất cả
 * handler/controller, các model chính được thừa kế từ base model này
 *
 * @author namnq
 */
public abstract class BaseModel {

    private static final Logger _Logger = ZLogger.getLogger(BaseModel.class);

    public abstract void process(HttpServletRequest req, HttpServletResponse resp);

    protected TClientPoolConfig.ConnConfig getConnectionConfig(String serviceName) {
        ThriftServers.Config config = new ThriftServers.Config();

        config.host = ZConfig.Instance.getString(ThriftServers.class, serviceName, "host", "127.0.0.1");
        config.port = ZConfig.Instance.getInt(ThriftServers.class, serviceName, "port", 8090);

        return new TClientPoolConfig.ConnConfig(
                config.host, config.port, config.framed, false, 50000,
                config.maxFrameSize, config.encryptVersionPriority
        );
    }

    /**
     * outAndClose: print data to client
     *
     * @param req
     * @param resp
     * @param content: String will be produced by content.toString()
     * @return
     */
    protected boolean outAndClose(HttpServletRequest req, HttpServletResponse resp, Object content) {
        boolean result = false;
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print(content);
            result = true;
        } catch (Exception ex) {
            _Logger.error(ex.getMessage() + " while processing URI \"" + req.getRequestURI() + "?" + req.getQueryString() + "\"", ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return result;
    }

    /**
     * prepareHeaderHtml: set http header for html content (text/html;
     * charset=UTF-8)
     *
     * @param resp
     */
    protected void prepareHeaderHtml(HttpServletResponse resp) {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=UTF-8");
    }

    /**
     * prepareHeaderJs: set http header for javascript content (text/javascript;
     * charset=UTF-8)
     *
     * @param resp
     */
    protected void prepareHeaderJs(HttpServletResponse resp) {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/javascript; charset=UTF-8");
    }
}
