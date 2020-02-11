/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.servers;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import com.vng.zing.jettyserver.WebServers;
import com.vng.zing.serverchain.handlers.HAddAccountHandler;
import com.vng.zing.serverchain.handlers.HLoginHandler;
import com.vng.zing.serverchain.handlers.HLogoutHandler;
import com.vng.zing.serverchain.handlers.HRemoveAccountHandler;
import com.vng.zing.serverchain.handlers.HShowInfoHandler;
import com.vng.zing.serverchain.handlers.HUpgradeHandler;

/**
 *
 * @author namnq
 */
public class HServers {

    public boolean setupAndStart() {
        WebServers servers = new WebServers(System.getProperty("project.name"));

        final ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(HLoginHandler.class, "/account/login");
        servletHandler.addServletWithMapping(HShowInfoHandler.class, "/user/info");
        servletHandler.addServletWithMapping(HLogoutHandler.class, "/user/logout");
        servletHandler.addServletWithMapping(HUpgradeHandler.class, "/user/upgrade");
        servletHandler.addServletWithMapping(HAddAccountHandler.class, "/account/register");
        servletHandler.addServletWithMapping(HRemoveAccountHandler.class, "/admin/remove");

        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("./webcontent");
        resourceHandler.setEtags(false);

        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.setWelcomeFiles(new String[]{"index.html"});
        contextHandler.setHandler(resourceHandler);

        final SessionHandler sessionHandler = contextHandler.getSessionHandler();
        sessionHandler.setHandler(servletHandler);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{contextHandler, sessionHandler});

        servers.setup(handlers);

//        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        contextHandler.setContextPath("/");
//        contextHandler.setWelcomeFiles(new String[]{"html/index.html"});
//        SessionHandler sessionHandler = contextHandler.getSessionHandler();
//        servers.setup(contextHandler);
//        servers.setup(sessionHandler);
//
//        String pwdPath = System.getProperty("project.webcontent");
//        ServletHolder holder = new ServletHolder("default", DefaultServlet.class);
//        holder.setInitParameter("resourceBase", pwdPath);
//        holder.setInitParameter("dirAllowed", "true");
//
//        contextHandler.addServlet(holder, "/");
//        contextHandler.addServlet(HLoginHandler.class, "/account/login");
//        contextHandler.addServlet(HShowInfoHandler.class, "/user/info");
//        contextHandler.addServlet(HLogoutHandler.class, "/user/logout");
//        contextHandler.addServlet(HUpgradeHandler.class, "/user/upgrade");
//        contextHandler.addServlet(HAddAccountHandler.class, "/account/register");
//        contextHandler.addServlet(HRemoveAccountHandler.class, "/admin/remove");
        return servers.start();
    }
}
