/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.thrift.client;

import org.apache.thrift.TException;

import com.vng.zing.thrift.resource.TReadService.Client;
import com.vng.zing.thrift.resource.TReadService.Iface;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thriftpool.TClientPool;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class TReadServiceClient extends TBaseServiceClient<Client> implements Iface {

    public TReadServiceClient(String serviceName) {
        super(TReadServiceClient.class, new Client.Factory(), serviceName);
    }

    @Override
    public TUserResult authenticate(String username, String password) throws TException {
        TClientPool<Client> clientPool = getClientPool();
        TUserResult result = new TUserResult();

        Client client = clientPool.borrowClient();
        if (client == null) {
            result.setError(ECode.BAD_CONNECTION.getValue());
        } else {
            result = client.authenticate(username, password);
            clientPool.returnClient(client);
        }

        return result;
    }

    @Override
    public TUserResult findById(int uId) throws TException {
        TClientPool<Client> clientPool = getClientPool();
        TUserResult result = new TUserResult();

        Client client = clientPool.borrowClient();
        if (client == null) {
            result.setError(ECode.BAD_CONNECTION.getValue());
        } else {
            result = client.findById(uId);
            clientPool.returnClient(client);
        }

        return result;
    }

    @Override
    public TUserResult findByUsername(String username) throws TException {
        TClientPool<Client> clientPool = getClientPool();
        TUserResult result = new TUserResult();

        Client client = clientPool.borrowClient();
        if (client == null) {
            result.setError(ECode.BAD_CONNECTION.getValue());
        } else {
            result = client.findByUsername(username);
            clientPool.returnClient(client);
        }

        return result;
    }
}
