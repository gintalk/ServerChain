/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.cache;

import com.vng.zing.media.common.cache.GCacheWrapper;

/**
 *
 * @author namnh16
 * @param <KType>: Type of key
 * @param <VType>: Type of value
 */
public class BaseCache<KType, VType> {

    private final GCacheWrapper<KType, VType> CACHE;

    protected BaseCache(String instanceName) {
        CACHE = new GCacheWrapper<>(instanceName);
    }

    public void put(KType key, VType value) {
        CACHE.put(key, value);
    }

    public VType get(KType key) {
        return CACHE.get(key);
    }

    public void remove(KType key) {
        CACHE.remove(key);
    }
}
