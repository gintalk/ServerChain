/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.util.HashMap;

import com.vng.zing.engine.dal.TokenDal;
import com.vng.zing.engine.dal.UserDal;
import com.vng.zing.engine.type.KVPair;
import com.vng.zing.media.common.utils.CommonUtils;
import com.vng.zing.serverchain.cache.IntTUserResultCache;
import com.vng.zing.serverchain.cache.StringPairCache;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class TBaseModel {

    protected static final IntTUserResultCache idCache = new IntTUserResultCache();
    protected static final StringPairCache usernameCache = new StringPairCache();

    protected TUserResult queryIfExist(String username) {
        return queryIfExist(username, "");
    }

    protected TUserResult queryIfExist(String username, String encrPassword) {
        TUserResult userResult;
        KVPair<Integer, String> cacheResult = usernameCache.get(username);

        if (cacheResult == null) {
            userResult = new TUserResult();
            HashMap<String, Object> tokenMap = TokenDal.INSTANCE.getItemAsMap(username, encrPassword);

            if (tokenMap == null) {
                userResult.setError(ECode.WRONG_AUTH.getValue());
            } else {
                int uId = (int) tokenMap.get("id");
                userResult = queryIfExist(uId);

                if (userResult.getError() == ECode.C_SUCCESS.getValue()) {
                    usernameCache.put(username, new KVPair<>(uId, encrPassword));
                }
            }
        } else {
            if (CommonUtils.isEmpty(encrPassword) || encrPassword.equals(cacheResult.getValue())) {
                userResult = idCache.get((int) cacheResult.getKey());
                if (userResult == null) {
                    userResult = new TUserResult(ECode.WRONG_AUTH.getValue());
                    usernameCache.remove(username);
                }
            } else {
                userResult = new TUserResult(ECode.WRONG_AUTH.getValue());
            }
        }

        return userResult;
    }

    protected TUserResult queryIfExist(int uId) {
        TUserResult userResult = idCache.get(uId);
        if (userResult == null) {
            userResult = new TUserResult();
            HashMap<String, Object> userMap = UserDal.INSTANCE.getItemAsMap(uId);

            if (userMap == null) {
                userResult.setError(ECode.NOT_EXIST.getValue());
            } else {
                userResult.setError(ECode.C_SUCCESS.getValue());
                userResult.setValue(Utils.mapToUser(userMap));

                idCache.put(uId, userResult);
            }
        }

        return userResult;
    }

    protected void updateRelevantCache(String username, TUserResult userResult) {
        updateRelevantCache(username, "", userResult);
    }

    protected void updateRelevantCache(String username, String encrPassword, TUserResult userResult) {
        KVPair<Integer, String> cacheResult = usernameCache.get(username);
        if (cacheResult != null) {
            cacheResult.setValue(encrPassword);
            usernameCache.put(username, cacheResult);
            updateRelevantCache(cacheResult.getKey(), userResult);
        }
    }

    protected void updateRelevantCache(int uId, TUserResult userResult) {
        TUserResult cacheResult = idCache.get(uId);
        if (cacheResult != null) {
            cacheResult = userResult;
            idCache.put(uId, cacheResult);
        }
    }

    protected void clearRelevantCache(String username) {
        KVPair<Integer, TUserResult> cacheResult = usernameCache.get(username);
        usernameCache.remove(username);

        if (cacheResult != null) {
            clearRelevantCache(cacheResult.getKey());
        }
    }

    protected void clearRelevantCache(int uId) {
        TUserResult cacheResult = idCache.get(uId);
        idCache.remove(uId);

        if (cacheResult != null) {
            clearRelevantCache(cacheResult.getValue().getUsername());
        }
    }
}
