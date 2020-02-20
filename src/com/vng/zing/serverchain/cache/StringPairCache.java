/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.cache;

import com.vng.zing.engine.type.KVPair;

/**
 *
 * @author namnh16
 */
public class StringPairCache extends BaseCache<String, KVPair> {

    public StringPairCache() {
        super("config_info");
    }
}
