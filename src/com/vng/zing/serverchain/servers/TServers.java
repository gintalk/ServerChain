/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.servers;


import org.apache.thrift.TProcessor;

import com.vng.zing.thriftserver.ThriftServers;

/**
 *
 * @author namnq
 */
public class TServers {

    private TProcessor _processor;
    private String _serviceName;

    public TServers(TProcessor processor, String serviceName) {
        this._processor = processor;
        this._serviceName = serviceName;
    }

    public boolean setupAndStart() {
        ThriftServers servers = new ThriftServers(_serviceName);

        servers.setup(_processor);

        return servers.start();
    }
}
