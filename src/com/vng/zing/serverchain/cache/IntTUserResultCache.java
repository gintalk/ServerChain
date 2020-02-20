/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.cache;

import com.vng.zing.thrift.resource.TUserResult;

/**
 *
 * @author namnh16
 */
public class IntTUserResultCache extends BaseCache<Integer, TUserResult> {

    public IntTUserResultCache() {
        super("config_info");
    }
}
