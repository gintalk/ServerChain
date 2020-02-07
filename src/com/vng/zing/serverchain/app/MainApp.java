/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.app;

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
                System.setProperty("name", "serverchain");
                
		HServers hServers = new HServers();
		if (!hServers.setupAndStart()) {
			System.err.println("Could not start http servers! Exit now.");
			System.exit(1);
		}

		///
		///thrift servers
		///
		TServers tServers = new TServers();
		if (!tServers.setupAndStart()) {
			System.err.println("Could not start thrift servers! Exit now.");
			System.exit(1);
		}
	}
}
