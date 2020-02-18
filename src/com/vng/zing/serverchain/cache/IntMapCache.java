/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.cache;

import java.util.HashMap;

/**
 *
 * @author namnh16
 */
public class IntMapCache extends BaseCache<Integer, HashMap<String, Object>> {

    public static final IntMapCache INSTANCE = new IntMapCache();

    private IntMapCache() {
        super();
    }
}
