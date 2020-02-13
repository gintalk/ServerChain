/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.type;

/**
 *
 * @author namnh16
 * @param <String>
 * @param <Object>
 */
public class Pair<String, Object> {

    private final String KEY;
    private final Object VALUE;

    public Pair(String key, Object value) {
        KEY = key;
        VALUE = value;
    }

    public String getKey() {
        return KEY;
    }

    public Object getValue() {
        return VALUE;
    }
}
