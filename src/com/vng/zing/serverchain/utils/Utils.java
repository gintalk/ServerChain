/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.thrift.resource.UserType;

/**
 *
 * @author namnh16
 */
public class Utils {

    private static final Logger LOGGER = ZLogger.getLogger(Utils.class);

    public static java.sql.Date getCurrentSQLDate() {
        return new Date(new java.util.Date().getTime());
    }

    public static UserType toUserType(String type) {
        return UserType.valueOf(type);
    }

    public static String toString(java.sql.Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String toString(UserType type) {
        switch (type.getValue()) {
            case 0:
                return "REGULAR";
            case 1:
                return "PREMIUM";
            case 2:
                return "ADMIN";
            default:
                return "UNSUPORRTED";
        }
    }

    public static String md5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] mdBytes = md.digest(password.getBytes("UTF-8"));

            return getString(mdBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return "";
    }

    private static String getString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            String hex = Integer.toHexString((int) 0x00FF & b);

            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }

        return sb.toString();
    }

    public static User mapToUser(HashMap<String, Object> userMap) {
        User user = new User();
        user.setFieldValue(user.fieldForId(1), userMap.get("id"));
        user.setFieldValue(user.fieldForId(2), userMap.get("name"));
        user.setFieldValue(user.fieldForId(3), UserType.valueOf((String) userMap.get("type")));
        user.setFieldValue(user.fieldForId(4), toString((java.sql.Date) userMap.get("joinDate")));

        return user;
    }
}
