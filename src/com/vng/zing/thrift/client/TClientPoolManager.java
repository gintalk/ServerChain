/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.thrift.client;

/**
 *
 * @author namnh16
 */
public class TClientPoolManager {

    private static final Object LOCK_OBJECT = new Object();

    private static TReadServiceClient readServiceClient = null;
    private static TWriteServiceClient writeServiceClient = null;

    public static TReadServiceClient getReadServiceClient() {
        if (readServiceClient != null) {
            return readServiceClient;
        }

        synchronized (LOCK_OBJECT) {
            if (readServiceClient == null) {
                readServiceClient = new TReadServiceClient(System.getProperty("project.name"));
            }
        }

        return readServiceClient;
    }

    public static TWriteServiceClient getWriteServiceClient() {
        if (writeServiceClient != null) {
            return writeServiceClient;
        }

        synchronized (LOCK_OBJECT) {
            if (writeServiceClient == null) {
                writeServiceClient = new TWriteServiceClient(System.getProperty("project.name"));
            }
        }

        return writeServiceClient;
    }
}
