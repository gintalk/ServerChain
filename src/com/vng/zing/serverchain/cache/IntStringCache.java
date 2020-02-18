/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.cache;

/**
 *
 * @author namnh16
 */
public class IntStringCache extends BaseCache<Integer, String> {

    public static final IntStringCache INSTANCE = new IntStringCache();

    private IntStringCache() {
        super();
    }
}
