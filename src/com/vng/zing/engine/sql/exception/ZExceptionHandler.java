/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.sql.exception;

import com.vng.zing.common.ZErrorDef;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.media.common.thrift.TI32Result;
import com.vng.zing.resource.thrift.TZException;

/**
 *
 * @author namnh16
 */
public class ZExceptionHandler {

    private static final Logger LOGGER = ZLogger.getLogger(ZExceptionHandler.class);
    public static final ZExceptionHandler INSTANCE = new ZExceptionHandler();

    private ZExceptionHandler() {

    }

    public enum State {
        GENERAL,
        SQL,
        INVALID_TOKEN,
        MISSING_USER,
        ADD_TOKEN_FAILED,
        ADD_USER_FAILED,
        REMOVE_TOKEN_FAILED,
        UPDATE_TOKEN_FAILED,
        UPDATE_USER_FAILED,
        MAXIMUM_PRIVILEGE,
    }

//    public ZExceptionHandler(String message, State state, int sqlErrorCode) {
//        _message = message;
//        _state = state;
//        _sqlErrorCode = sqlErrorCode;
//    }
    public void prepareException(TZException tzex, Exception ex) {
        prepareException(tzex, ex.getMessage(), State.GENERAL, -1);
    }

    public void prepareException(TZException tzex, SQLException sqlex) {
        prepareException(tzex, sqlex.getMessage(), State.SQL, sqlex.getErrorCode());
    }

    public void prepareException(TZException tzex, State state) {
        prepareException(tzex, "", state);
    }

    public void prepareException(TZException tzex, String message, State state) {
        prepareException(tzex, message, state, 0);
    }

    public void prepareException(TZException tzex, String message, State state, int sqlErrorCode) {
        if (state == State.SQL) {
            LOGGER.error("SQL error code: " + sqlErrorCode);
            LOGGER.error(message);
        } else if (state == State.GENERAL
            || state == State.MISSING_USER
            || state == State.ADD_USER_FAILED) {
            LOGGER.error(state + ": " + message);
        }

        String webMessage = getWebMessage(state);
        tzex.setFieldValue(tzex.fieldForId(1), message);
        tzex.setFieldValue(tzex.fieldForId(2), webMessage);
    }

    public void prepareResult(TI32Result result, SQLException ex) {
        LOGGER.debug(ex.getMessage());
        result.setFieldValue(result.fieldForId(1), ZErrorDef.FAIL);
        result.setFieldValue(result.fieldForId(3), getSqlMessage(ex.getErrorCode()));
    }

//    @Override
//    public String getMessage() {
//        return _message;
//    }
    private String getSqlMessage(int sqlErrorCode) {
        switch (sqlErrorCode) {
            case 1062:
                return "A user with that username exist. Please choose a different username";
            default:
                return "Unexpected error, please report";
        }
    }

    private String getWebMessage(State state) {
        switch (state) {
            case SQL:
            case GENERAL:
            case MISSING_USER:
            case ADD_USER_FAILED:
                return "Internal error, please report";
            case REMOVE_TOKEN_FAILED:
                return "Token does not exist";
            case INVALID_TOKEN:
                return "Incorrect username or password";
            case ADD_TOKEN_FAILED:
                return "A user with that username exists. Please choose a different username";
            case MAXIMUM_PRIVILEGE:
                return "Unable to upgrade further";
            default:
                return "Unexpected error, please report";
        }
    }
}
