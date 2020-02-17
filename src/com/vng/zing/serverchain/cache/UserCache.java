/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.cache;

import com.vng.zing.resource.thrift.User;
import java.util.HashMap;

/**
 *
 * @author namnh16
 */
public class UserCache extends BaseCache<Integer, HashMap<String, Object>> {

    public static final UserCache INSTANCE = new UserCache();

    private UserCache() {
        super();
    }
}
