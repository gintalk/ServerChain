/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.servers;

import com.vng.zing.jettyserver.WebServers;
import com.vng.zing.serverchain.handlers.HAddAccountHandler;
import com.vng.zing.serverchain.handlers.HLogInHandler;
import com.vng.zing.serverchain.handlers.HLogOutHandler;
import com.vng.zing.serverchain.handlers.HRemoveAccountHandler;
import com.vng.zing.serverchain.handlers.HShowInfoHandler;
import com.vng.zing.serverchain.handlers.HUpgradeHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
/**
 *
 * @author namnq
 */
public class HServers {

	public boolean setupAndStart() {
		WebServers servers = new WebServers(System.getProperty("project.name"));
                
                ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
                contextHandler.setContextPath("/");
                contextHandler.setWelcomeFiles(new String[]{"html/index.html"});
                SessionHandler sessionHandler = contextHandler.getSessionHandler();
                servers.setup(contextHandler);
                servers.setup(sessionHandler);
                
                String pwdPath = System.getProperty("project.webcontent");
                ServletHolder holder = new ServletHolder("default", DefaultServlet.class);
                holder.setInitParameter("resourceBase", pwdPath);
                holder.setInitParameter("dirAllowed", "true");
                
                contextHandler.addServlet(holder, "/");
                contextHandler.addServlet(HLogInHandler.class, "/account/login");
                contextHandler.addServlet(HShowInfoHandler.class, "/user/info");
                contextHandler.addServlet(HLogOutHandler.class, "/user/logout");
                contextHandler.addServlet(HUpgradeHandler.class, "/user/upgrade");
                contextHandler.addServlet(HAddAccountHandler.class, "/account/register");
                contextHandler.addServlet(HRemoveAccountHandler.class, "/admin/remove");
                
		return servers.start();
	}
}
