/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import java.util.HashMap;

import com.vng.zing.engine.sql.exception.ZExceptionHandler;
import com.vng.zing.engine.type.Pair;
import com.vng.zing.resource.thrift.TZException;

/**
 *
 * @author namnh16
 */
public interface BaseDal {

    static final String AUTHENTICATE_DB_INST = "authenticatedb";
    static final String APPLICATION_DB_INST = "applicationdb";

    public HashMap<String, Object> getItemAsMap(int i) throws TZException;

    public HashMap<String, Object> getItemAsMap(String s) throws TZException;

    public int addItemAutoKey(Object... params) throws TZException;

    public boolean addItem(Object... params) throws TZException;

    public boolean removeItem(int i) throws TZException;

    public boolean updateItem(int i, Pair... pairs) throws TZException;
}
