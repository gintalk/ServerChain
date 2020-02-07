/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.servers;

import com.vng.zing.calc.thrift.Calc;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.serverchain.handlers.TAuthenticatorHandler;
import com.vng.zing.thriftserver.ThriftServers;
import com.vng.zing.serverchain.handlers.TCalcHandler;

/**
 *
 * @author namnq
 */
public class TServers {

	public boolean setupAndStart() {
		ThriftServers servers = new ThriftServers(System.getProperty("name"));
		Authenticator.Processor processor = new Authenticator.Processor(new TAuthenticatorHandler());
		servers.setup(processor);
		return servers.start();
	}
}
