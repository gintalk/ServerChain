/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.thrift.client;

import org.apache.log4j.Logger;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;

import com.vng.zing.common.ZCommonDef;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.thriftpool.TClientPool;
import com.vng.zing.thriftpool.ZClientPoolUtil;

/**
 *
 * @author namnh16
 * @param <SType>
 */
public abstract class TBaseServiceClient<SType extends TServiceClient> {

    protected final Class THIS_CLASS;
    protected final Logger LOGGER;
    protected final TServiceClientFactory CLIENT_FACTORY;
    protected final String INSTANCE_NAME;
    protected TClientPool.BizzConfig _bizzConfig;

    protected TBaseServiceClient(Class clazz, TServiceClientFactory clientFactory, String instanceName) {
        THIS_CLASS = clazz;
        LOGGER = ZLogger.getLogger(THIS_CLASS);
        CLIENT_FACTORY = clientFactory;
        INSTANCE_NAME = instanceName;

        init();
    }

    private void init() {
        ZClientPoolUtil.SetDefaultPoolProp(
            THIS_CLASS,
            INSTANCE_NAME,
            null,
            null,
            ZCommonDef.TClientTimeoutMilisecsDefault,
            ZCommonDef.TClientNRetriesDefault,
            ZCommonDef.TClientMaxRdAtimeDefault,
            ZCommonDef.TClientMaxWrAtimeDefault
        );
        ZClientPoolUtil.GetListPools(THIS_CLASS, INSTANCE_NAME, CLIENT_FACTORY);
        _bizzConfig = ZClientPoolUtil.GetBizzCfg(THIS_CLASS, INSTANCE_NAME);
    }

    protected TClientPool<SType> getClientPool() {
        return (TClientPool<SType>) ZClientPoolUtil.GetPool(THIS_CLASS, INSTANCE_NAME);
    }

    protected TClientPool.BizzConfig getBizzConfig() {
        return _bizzConfig;
    }
}
