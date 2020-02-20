/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.type;

/**
 *
 * @author namnh16
 * @param <KType>
 * @param <VType>
 */
public class KVPair<KType, VType> {

    private KType KEY;
    private VType VALUE;

    public KVPair() {
        this(null);
    }

    public KVPair(KType key) {
        this(key, null);
    }

    public KVPair(KType key, VType value) {
        KEY = key;
        VALUE = value;
    }

    public void setKey(KType key) {
        KEY = key;
    }

    public KType getKey() {
        return KEY;
    }

    public void setValue(VType value) {
        VALUE = value;
    }

    public VType getValue() {
        return VALUE;
    }
}
