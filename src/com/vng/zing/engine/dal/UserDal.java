/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.vng.zing.engine.sql.dao.UserDao;
import com.vng.zing.engine.type.KVPair;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.thrift.resource.TI32Result;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class UserDal implements BaseDal {

    private static final Logger LOGGER = ZLogger.getLogger(UserDal.class);

    private final UserDao _userDao;
    public static final UserDal INSTANCE = new UserDal();

    private UserDal() {
        _userDao = new UserDao(APPLICATION_DB_INST);
    }

    @Override
    public HashMap<String, Object> getItemAsMap(int id) {
        List<HashMap<String, Object>> rows = _userDao.selectAsListMap(
            "SELECT id, username, name, type, joinDate FROM User WHERE id=?",
            id
        );
        if (rows != null) {
            return rows.get(0);
        }

        return null;
    }

    @Override
    public HashMap<String, Object> getItemAsMap(String username) {
        List<HashMap<String, Object>> rows = _userDao.selectAsListMap(
            "SELECT id, username, name, type, joinDate FROM User WHERE username=?",
            username
        );
        if (rows != null) {
            return rows.get(0);
        }

        return null;
    }

    @Override
    public HashMap<String, Object> getItemAsMap(String s, String ss) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TI32Result addItemAutoKey(Object... params) {
        TI32Result result = _userDao.insert(
            "INSERT INTO User VALUES(?,?,?,?,?)",
            true,
            params
        );
        if (result.getError() != ECode.C_SUCCESS.getValue()) {
            LOGGER.error(result.getError());
            LOGGER.error(result.getExtData());
        }

        return result;
    }

    @Override
    public TI32Result addItem(Object... params) {
        TI32Result result = _userDao.insert(
            "INSERT INTO User VALUES(?,?,?,?,?)",
            false,
            params
        );
        if (result.getError() != ECode.C_SUCCESS.getValue()) {
            LOGGER.error(result.getError());
            LOGGER.error(result.getExtData());
        }

        return result;
    }

    @Override
    public TI32Result removeItem(int i) {
        throw new UnsupportedOperationException("Can only cascade row removal from Token");
    }

    @Override
    public TI32Result updateItem(int id, KVPair... pairs) {
        Object[] objects = new Object[pairs.length + 1];
        StringBuilder sb = new StringBuilder("UPDATE User SET ");
        for (int i = 0; i < pairs.length; i++) {
            sb.append(pairs[i].getKey());
            sb.append("=?");
            if (i < pairs.length - 1) {
                sb.append(",");
            }
            sb.append(" ");
            objects[i] = pairs[i].getValue();
        }
        sb.append("WHERE id=?");
        objects[pairs.length] = id;

        TI32Result result = _userDao.update(sb.toString(), objects);
        if (result.getError() != ECode.C_SUCCESS.getValue()) {
            LOGGER.error(result.getError());
            LOGGER.error(result.getExtData());
        }

        return result;
    }
}
