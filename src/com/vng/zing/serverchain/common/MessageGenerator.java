/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.common;

import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class MessageGenerator {

    public static String getMessage(ECode ecode) {
        return getMessage(ecode.getValue());
    }

    public static String getMessage(int ecode) {
        String webMessage = "";

        if (ecode == ECode.C_FAIL.getValue()
            || ecode == ECode.EXCEPTION.getValue()) {
            webMessage = "Internal error, please report";
        } else if (ecode == ECode.WRONG_AUTH.getValue()) {
            webMessage = "Incorrect username or password";
        } else if (ecode == ECode.ALREADY_EXIST.getValue()) {
            webMessage = "A user with that username already exists. Please choose a different username";
        } else if (ecode == ECode.REACH_MAX.getValue()) {
            webMessage = "Already a PREMIUM user";
        } else if (ecode == ECode.NOT_EXIST.getValue()) {
            webMessage = "The user with that ID does not exist";
        } else if (ecode == ECode.UNLOADED.getValue()) {
            webMessage = "Must log in first";
        } else if (ecode == ECode.NOT_ALLOW.getValue()) {
            webMessage = "Reserved for ADMIN";
        }

        return webMessage;
    }
}
