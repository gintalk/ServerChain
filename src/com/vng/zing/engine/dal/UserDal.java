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
import com.vng.zing.serverchain.cache.IntMapCache;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
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
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        profiler.push(this.getClass(), "getItemAsMap");
        try {
            if (id < 1) {
                return null;
            }

            HashMap<String, Object> cacheResult = IntMapCache.INSTANCE.get(id);
            if (cacheResult == null) {
                List<HashMap<String, Object>> rows = _userDao.selectAsListMap(
                    "SELECT id, name, type, joinDate FROM User WHERE id=?",
                    id
                );
                if (rows != null) {
                    profiler.push(this.getClass(), "getItemAsMap.misscache");

                    cacheResult = rows.get(0);
                    IntMapCache.INSTANCE.put(id, cacheResult);
                    profiler.pop(this.getClass(), "getItemAsMap.misscache");

                }
            }
            return cacheResult;
        } catch (Exception ex) {
            return null;
        } finally {
            profiler.pop(this.getClass(), "getItemAsMap");
        }

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
    public TI32Result addItemAutoKey(Object... params) {
        TI32Result result = _userDao.insert(
            "INSERT INTO User VALUES(?,?,?,?)",
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
            "INSERT INTO User VALUES(?,?,?,?)",
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
        throw new UnsupportedOperationException("Can only cascade row removal from UserToken");
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
        } else {
            IntMapCache.INSTANCE.remove(id);
        }

        return result;
    }
}
