/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.sql.exception;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;

/**
 *
 * @author namnh16
 */
public class ZException extends Exception {

    private static final Logger LOGGER = ZLogger.getLogger(ZException.class);

    private final String _message;
    private final State _state;
    private final int _sqlErrorCode;

    public enum State {
        GENERAL,
        SQL,
        INVALID_TOKEN,
        MISSING_USER,
        ADD_TOKEN_FAILED,
        ADD_USER_FAILED,
        REMOVE_TOKEN_FAILED,
        MAXIMUM_PRIVILEGE,
    }

    public ZException(Exception ex) {
        this(ex.getMessage());
    }

    public ZException(SQLException sqlex) {
        this(sqlex.getMessage(), State.SQL, sqlex.getErrorCode());
    }

    public ZException(String message) {
        this(message, State.GENERAL, 0);
    }

    public ZException(String message, State state) {
        this(message, state, 0);
    }

    public ZException(String message, State state, int sqlErrorCode) {
        _message = message;
        _state = state;
        _sqlErrorCode = sqlErrorCode;
    }

    @Override
    public String getMessage() {
        return _message;
    }

    public String getWebMessage() {
        switch (_state) {
            case SQL:
                LOGGER.error("SQL error code:" + String.valueOf(_sqlErrorCode));
            case GENERAL:
            case MISSING_USER:
            case ADD_USER_FAILED:
                LOGGER.error(_message);
                return "Internal error, please report";
            case REMOVE_TOKEN_FAILED:
                return "Token does not exist";
            case INVALID_TOKEN:
                return "Incorrect username or password";
            case ADD_TOKEN_FAILED:
                return "A user with that username existed. Please choose a different username";
            case MAXIMUM_PRIVILEGE:
                return "Unable to upgrade further";
            default:
                return "Undocumented error, please report";
        }
    }
}
