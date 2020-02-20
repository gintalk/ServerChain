/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thrift.resource.TUserResult;

/**
 *
 * @author namnh16
 */
public class TReadServiceModel extends TBaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(TReadServiceModel.class);
    public static final TReadServiceModel INSTANCE = new TReadServiceModel();

    private TReadServiceModel() {

    }

    public TUserResult authenticate(String username, String password) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "TReadServiceModel.authenticate");

        try {
            return queryIfExist(username, password);
        } finally {
            profiler.pop(this.getClass(), "TRadServiceModel.authenticate");
        }

//        String cacheKey = username + password;
//        TUserResult cacheResult = stringTUserResultCache.get(cacheKey);
//        if (cacheResult == null) {
//            profiler.push(this.getClass(), "TReadServiceModel.authenticate.cacheMiss");
//
//            cacheResult = new TUserResult();
//            HashMap<String, Object> tokenMap = TokenDal.INSTANCE.getItemAsMap(username, password);
//
//            if (tokenMap == null) {
//                cacheResult.setError(ECode.WRONG_AUTH.getValue());
//            } else {
//                HashMap<String, Object> userMap = UserDal.INSTANCE.getItemAsMap((int) tokenMap.get("id"));
//                if (userMap == null) {
//                    cacheResult.setError(ECode.C_FAIL.getValue());
//                } else {
//                    cacheResult.setError(ECode.C_SUCCESS.getValue());
//                    cacheResult.setValue(Utils.mapToUser(userMap));
//
//                    stringTUserResultCache.put(cacheKey, cacheResult);
//                }
//            }
//
//            profiler.pop(this.getClass(), "TReadServiceModel.authenticate.cacheMiss");
//        }
//
//        profiler.pop(this.getClass(), "TReadServiceModel.authenticate");
//        return cacheResult;
    }

    public TUserResult findById(int uId) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "TReadServiceModel");

        try {
            return queryIfExist(uId);
        } finally {
            profiler.pop(this.getClass(), "TReadServiceModel.findById");
        }

//        int cacheKey = uId;
//        TUserResult cacheResult = idTUserResultCache.get(cacheKey);
//        if (cacheResult == null) {
//            profiler.push(this.getClass(), "TReadServiceModel.cacheMiss");
//
//            cacheResult = new TUserResult();
//            HashMap<String, Object> userMap = UserDal.INSTANCE.getItemAsMap(uId);
//
//            if (userMap == null) {
//                cacheResult.setError(ECode.NOT_EXIST.getValue());
//            } else {
//                cacheResult.setError(ECode.C_SUCCESS.getValue());
//                cacheResult.setValue(Utils.mapToUser(userMap));
//
//                idTUserResultCache.put(cacheKey, cacheResult);
//            }
//
//            profiler.pop(this.getClass(), "TReadServiceModel.cacheMiss");
//        }
//
//        profiler.pop(this.getClass(), "TReadServiceModel");
//        return cacheResult;
    }

    public TUserResult findByUsername(String username) {
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "TReadServiceModel.findByUsername");

        try {
            return queryIfExist(username);
        } finally {
            profiler.pop(this.getClass(), "TReadServiceModel.findByUsername");
        }

//        TUserResult result= new TUserResult();
//        HashMap<String, Object> userMap = UserDal.INSTANCE.getItemAsMap(username);
//        if(userMap == null){
//            result.setError(ECode.NOT_EXIST.getValue());
//        }
//        else{
//            result.setError(ECode.C_SUCCESS.getValue());
//            result.setValue(Utils.mapToUser(userMap));
//        }
//        
//        return result;
    }
}
