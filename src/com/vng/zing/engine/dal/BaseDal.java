/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import java.util.HashMap;

import com.vng.zing.engine.type.KVPair;
import com.vng.zing.thrift.resource.TI32Result;

/**
 *
 * @author namnh16
 */
public interface BaseDal {

    static final String AUTHENTICATE_DB_INST = "authenticatedb";
    static final String APPLICATION_DB_INST = "applicationdb";

    public HashMap<String, Object> getItemAsMap(int i);

    public HashMap<String, Object> getItemAsMap(String s);

    public HashMap<String, Object> getItemAsMap(String s, String ss);

    public TI32Result addItemAutoKey(Object... params);

    public TI32Result addItem(Object... params);

    public TI32Result removeItem(int i);

    public TI32Result updateItem(int i, KVPair... pairs);
}
