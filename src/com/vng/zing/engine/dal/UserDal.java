/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.vng.zing.common.ZErrorDef;
import com.vng.zing.engine.sql.dao.UserDao;
import com.vng.zing.engine.type.Pair;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.media.common.thrift.TI32Result;
import com.vng.zing.serverchain.cache.UserMapCache;

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
        if (id < 1) {
            return null;
        }

        HashMap<String, Object> cacheResult = UserMapCache.INSTANCE.get(id);
        if (cacheResult == null) {
            List<HashMap<String, Object>> rows = _userDao.selectAsListMap(
                "SELECT id, name, type, joinDate FROM User WHERE id=?",
                id
            );
            if (rows != null) {
                cacheResult = rows.get(0);
                UserMapCache.INSTANCE.put(id, cacheResult);
            }
        }

        return cacheResult;
    }

    @Override
    public HashMap<String, Object> getItemAsMap(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashMap<String, Object> getItemAsMap(String s, String ss) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int addItemAutoKey(Object... params) {
        if (params == null || params.length < 4) {
            return 0;
        }

        TI32Result result = _userDao.insert(
            "INSERT INTO User VALUES(?,?,?,?)",
            true,
            params
        );
        if (result.getError() == ZErrorDef.FAIL || result.getValue() < 1) {
            return 0;
        }

        return result.getValue();
    }

    @Override
    public boolean addItem(Object... params) {
        if (params == null || params.length < 4) {
            return false;
        }

        TI32Result result = _userDao.insert(
            "INSERT INTO User VALUES(?,?,?,?)",
            false,
            params
        );
        if (result.getError() == ZErrorDef.FAIL || result.getValue() < 1) {
            return false;
        }

        return true;
    }

    @Override
    public boolean removeItem(int i) {
        throw new UnsupportedOperationException("Can only cascade row removal from UserToken");
    }

    @Override
    public boolean updateItem(int id, Pair... pairs) {
        if (id < 1 || pairs.length < 1) {
            return false;
        }

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
        if (result.getError() == ZErrorDef.FAIL || result.getValue() < 1) {
            return false;
        }

        UserMapCache.INSTANCE.remove(id);
        return true;
    }
}
