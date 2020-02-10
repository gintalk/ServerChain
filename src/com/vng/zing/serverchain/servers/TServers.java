/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.servers;

import com.vng.zing.calc.thrift.Calc;
import com.vng.zing.resource.thrift.Account;
import com.vng.zing.resource.thrift.Application;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.serverchain.handlers.TAccountHandler;
import com.vng.zing.serverchain.handlers.TApplicationHandler;
import com.vng.zing.serverchain.handlers.TAuthenticatorHandler;
import com.vng.zing.thriftserver.ThriftServers;
import com.vng.zing.serverchain.handlers.TCalcHandler;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;

/**
 *
 * @author namnq
 */
public class TServers {
        private TProcessor _processor;
        private String _serviceName;
        
        public TServers(TProcessor processor, String serviceName){
            this._processor = processor;
            this._serviceName = serviceName;
        }

	public boolean setupAndStart() {
		ThriftServers servers = new ThriftServers(_serviceName);
                
		servers.setup(_processor);
                
		return servers.start();
	}
}
