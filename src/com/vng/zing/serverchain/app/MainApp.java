/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.app;

import java.util.HashMap;
import java.util.Map;

import org.rythmengine.Rythm;

import com.vng.zing.resource.thrift.TReadService;
import com.vng.zing.resource.thrift.TWriteService;
import com.vng.zing.serverchain.handlers.TReadServiceHandler;
import com.vng.zing.serverchain.handlers.TWriteServiceHandler;
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
        ///thrift servers: Read
        ///
        TServers tReadServers = new TServers(
            new TReadService.Processor(new TReadServiceHandler()),
            "ReadService");
        if (!tReadServers.setupAndStart()) {
            System.err.println("Could not start thrift authenticator servers! Exit now.");
            System.exit(1);
        }

        ///
        ///thrift servers: Write
        ///
        TServers tWriteServers = new TServers(
            new TWriteService.Processor(new TWriteServiceHandler()),
            "WriteService");
        if (!tWriteServers.setupAndStart()) {
            System.err.println("Could not start thrift application servers! Exit now.");
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
