/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.cache;

import com.vng.zing.media.common.thrift.TI32Result;

/**
 *
 * @author namnh16
 */
public class TI32ResultCache extends BaseCache<Object[], TI32Result>{
    
    public static final TI32ResultCache INSTANCE = new TI32ResultCache();

    private TI32ResultCache() {
        super();
    }
}
