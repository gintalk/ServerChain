/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.thrift.client;

import org.apache.thrift.TException;

import com.vng.zing.thrift.resource.TI32Result;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thrift.resource.TWriteService.Client;
import com.vng.zing.thrift.resource.TWriteService.Iface;
import com.vng.zing.thrift.resource.Token;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.thriftpool.TClientPool;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class TWriteServiceClient extends TBaseServiceClient<Client> implements Iface {

    public TWriteServiceClient(String serviceName) {
        super(TWriteServiceClient.class, new Client.Factory(), serviceName);
    }

    @Override
    public TUserResult upgrade(User user) throws TException {
        TClientPool<Client> clientPool = getClientPool();
        TUserResult result = new TUserResult();

        Client client = clientPool.borrowClient();
        if (client == null) {
            result.setError(ECode.BAD_CONNECTION.getValue());
        } else {
            result = client.upgrade(user);
            clientPool.returnClient(client);
        }

        return result;
    }

    @Override
    public TI32Result add(Token token, User user) throws TException {
        TClientPool<Client> clientPool = getClientPool();
        TI32Result result = new TI32Result();

        Client client = clientPool.borrowClient();
        if (client == null) {
            result.setError(ECode.BAD_CONNECTION.getValue());
        } else {
            result = client.add(token, user);
            clientPool.returnClient(client);
        }

        return result;
    }

    @Override
    public TI32Result remove(int uId) throws TException {
        TClientPool<Client> clientPool = getClientPool();
        TI32Result result = new TI32Result();

        Client client = clientPool.borrowClient();
        if (client == null) {
            result.setError(ECode.BAD_CONNECTION.getValue());
        } else {
            result = client.remove(uId);
            clientPool.returnClient(client);
        }

        return result;
    }
}
