/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.type;

/**
 *
 * @author namnh16
 * @param <String>
 * @param <VType>
 */
public class KVPair<String, VType> {

    private final String KEY;
    private final VType VALUE;

    public KVPair(String key, VType value) {
        KEY = key;
        VALUE = value;
    }

    public String getKey() {
        return KEY;
    }

    public VType getValue() {
        return VALUE;
    }
}
