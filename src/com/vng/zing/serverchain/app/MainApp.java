/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.app;

import java.util.HashMap;
import java.util.Map;

import org.rythmengine.Rythm;

import com.vng.zing.resource.thrift.Account;
import com.vng.zing.resource.thrift.Application;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.serverchain.handlers.TAccountHandler;
import com.vng.zing.serverchain.handlers.TApplicationHandler;
import com.vng.zing.serverchain.handlers.TAuthenticatorHandler;
import com.vng.zing.serverchain.servers.HServers;
import com.vng.zing.serverchain.servers.TServers;

/**
 *
 * @author namnq
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ///
        ///http servers
        ///
        init();

        HServers hServers = new HServers();
        if (!hServers.setupAndStart()) {
            System.err.println("Could not start http servers! Exit now.");
            System.exit(1);
        }

        ///
        ///thrift servers: Authenticator
        ///
        TServers tAuthServers = new TServers(
                new Authenticator.Processor(new TAuthenticatorHandler()),
                "Authenticator");
        if (!tAuthServers.setupAndStart()) {
            System.err.println("Could not start thrift authenticator servers! Exit now.");
            System.exit(1);
        }

        ///
        ///thrift servers: Application
        ///
        TServers tAppServers = new TServers(
                new Application.Processor(new TApplicationHandler()),
                "Application");
        if (!tAppServers.setupAndStart()) {
            System.err.println("Could not start thrift application servers! Exit now.");
            System.exit(1);
        }

        ///
        ///thrift servers: Account
        ///
        TServers tAccServers = new TServers(
                new Account.Processor(new TAccountHandler()),
                "Account");
        if (!tAccServers.setupAndStart()) {
            System.err.println("Could not start thrift account servers! Exit now.");
            System.exit(1);
        }
    }

    private static boolean _initialized = false;

    private static void init() {
        if (_initialized) {
            return;
        }
        System.setProperty("project.name", "serverchain");
        System.setProperty("project.dir", System.getProperty("user.dir"));
        System.setProperty("project.webdir", System.getProperty("project.dir") + "/webcontent");
        System.setProperty("project.static", System.getProperty("project.webdir") + "/static");
        System.setProperty("project.template", System.getProperty("project.webdir") + "/template/rythm/page");

        Map<String, Object> conf = new HashMap<>();
        conf.put("home.template", System.getProperty("project.template"));
        Rythm.init(conf);
        _initialized = true;
    }
}
