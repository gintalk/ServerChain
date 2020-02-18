/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.thrift.client;

import com.vng.zing.common.ZCommonDef;
import com.vng.zing.engine.dal.TokenDal;
import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.thrift.resource.TReadService.Client;
import com.vng.zing.thrift.resource.TReadService.Iface;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thriftpool.TClientPool;
import com.vng.zing.thriftpool.ZClientPoolUtil;
import com.vng.zing.zcommon.thrift.ECode;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author namnh16
 */
public class TReadServiceClient implements Iface {

    private static final Class THIS_CLASS = TReadServiceClient.class;
    private static final Logger LOGGER = ZLogger.getLogger(THIS_CLASS);
    private final String INSTANCE_NAME;
    private TClientPool.BizzConfig _bizzConfig;

    public TReadServiceClient(String serviceName) {
        INSTANCE_NAME = serviceName;
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
        ZClientPoolUtil.GetListPools(THIS_CLASS, INSTANCE_NAME, new Client.Factory());
        _bizzConfig = ZClientPoolUtil.GetBizzCfg(THIS_CLASS, INSTANCE_NAME);
    }

    private TClientPool<Client> getClientPool() {
        return (TClientPool<Client>) ZClientPoolUtil.GetPool(THIS_CLASS, INSTANCE_NAME);
    }

    private TClientPool.BizzConfig getBizzConfig() {
        return _bizzConfig;
    }

    @Override
    public TUserResult authenticate(String username, String password) throws TException {
        TClientPool<Client> clientPool = getClientPool();
        TClientPool.BizzConfig bizzConfig = getBizzConfig();

        TUserResult result = new TUserResult();
        for (int retry = 0; retry < bizzConfig.getNRetry(); retry++) {
            Client client = clientPool.borrowClient();
            if (client == null) {
                result.setError(ECode.BAD_CONNECTION.getValue());
            } else {
                client.authenticate(username, password);
                HashMap<String, Object> tokenMap = TokenDal.INSTANCE.getItemAsMap(username, password);
                if (tokenMap == null) {
                    result.setError(ECode.WRONG_AUTH.getValue());
                } else {
                    HashMap<String, Object> userMap = UserDal.INSTANCE.getItemAsMap((int) tokenMap.get("id"));
                    if (userMap == null) {
                        result.setError(ECode.C_FAIL.getValue());
                    } else {
                        result.setError(ECode.C_SUCCESS.getValue());
                        result.setValue(Utils.mapToUser(userMap));
                        break;
                    }
                }
            }
        }
        
        return result;
    }
}
