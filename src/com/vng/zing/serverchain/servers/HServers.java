/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.servers;

import com.vng.zing.jettyserver.WebServers;
import com.vng.zing.serverchain.handlers.LogInHandler;
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
		WebServers servers = new WebServers(System.getProperty("name"));
                
                ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
                contextHandler.setContextPath("/");
                contextHandler.setWelcomeFiles(new String[]{"index.html"});
                SessionHandler sessionHandler = contextHandler.getSessionHandler();
                servers.setup(contextHandler);
                servers.setup(sessionHandler);
                
                String pwdPath = System.getProperty("user.dir") + "/webcontent";
                ServletHolder holder = new ServletHolder("default", DefaultServlet.class);
                holder.setInitParameter("resourceBase", pwdPath);
                holder.setInitParameter("dirAllowed", "true");
                
                contextHandler.addServlet(holder, "/");
                contextHandler.addServlet(LogInHandler.class, "/account/login");
//                contextHandler.addServlet(ShowInfoServlet.class, "/user/info");
//                contextHandler.addServlet(LogOutServlet.class, "/user/logout");
//                contextHandler.addServlet(UpgradeServlet.class, "/user/upgrade");
//                contextHandler.addServlet(AddAccountServlet.class, "/account/register");
//                contextHandler.addServlet(RemoveAccountServlet.class, "/admin/remove");
                
		return servers.start();
	}
}
