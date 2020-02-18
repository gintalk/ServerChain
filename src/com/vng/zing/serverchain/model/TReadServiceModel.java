/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import com.vng.zing.engine.dal.TokenDal;
import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.zcommon.thrift.ECode;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author namnh16
 */
public class TReadServiceModel {

    private static final Logger LOGGER = ZLogger.getLogger(TReadServiceModel.class);
    public static final TReadServiceModel INSTANCE = new TReadServiceModel();

    private TReadServiceModel() {

    }

    public TUserResult authenticate(String username, String password) {
        
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        
        TUserResult result = new TUserResult();

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
            }
        }

        return result;
    }
}
