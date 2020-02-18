/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import java.util.HashMap;

import com.vng.zing.engine.type.Pair;

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

    public int addItemAutoKey(Object... params);

    public boolean addItem(Object... params);

    public boolean removeItem(int i);

    public boolean updateItem(int i, Pair... pairs);
}
