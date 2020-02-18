/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.vng.zing.common.ZErrorDef;
import com.vng.zing.engine.sql.dao.TokenDao;
import com.vng.zing.engine.type.Pair;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.media.common.thrift.TI32Result;
import com.vng.zing.media.common.utils.CommonUtils;

/**
 *
 * @author namnh16
 */
public class TokenDal implements BaseDal {

    private static final Logger LOGGER = ZLogger.getLogger(TokenDal.class);

    private final TokenDao _tokenDao;
    public static final TokenDal INSTANCE = new TokenDal();

    private TokenDal() {
        _tokenDao = new TokenDao(AUTHENTICATE_DB_INST);
    }

    @Override
    public HashMap<String, Object> getItemAsMap(int id) {
        if (id < 1) {
            return null;
        }

        List<HashMap<String, Object>> rows = _tokenDao.selectAsListMap(
            "SELECT id, username, password FROM UserToken WHERE id=?",
            id
        );
        if (rows != null) {
            return rows.get(0);
        }

        return null;
    }

    @Override
    public HashMap<String, Object> getItemAsMap(String username) {
        return getItemAsMap(username, "");
    }

    @Override
    public HashMap<String, Object> getItemAsMap(String username, String password) {
        if (CommonUtils.isEmpty(username)) {
            return null;
        }

        StringBuilder sql = new StringBuilder("SELECT id, username, password FROM UserToken WHERE username=?");
        List<HashMap<String, Object>> rows;
        if (!CommonUtils.isEmpty(password)) {
            sql.append(" AND password=?");
            rows = _tokenDao.selectAsListMap(sql.toString(), username, password);
        } else {
            rows = _tokenDao.selectAsListMap(sql.toString(), username);
        }
        if (rows != null) {
            return rows.get(0);
        }

        return null;
    }

    @Override
    public int addItemAutoKey(Object... params) {
        if (params == null || params.length < 2) {
            return 0;
        }

        TI32Result result = _tokenDao.insert(
            "INSERT INTO UserToken(username, password) VALUES(?,?)",
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
        if (params == null || params.length < 2) {
            return false;
        }

        TI32Result result = _tokenDao.insert(
            "INSERT INTO UserToken(username, password) VALUES(?,?)",
            false,
            params
        );
        if (result.getError() == ZErrorDef.FAIL || result.getValue() < 1) {
            return false;
        }

        return true;
    }

    @Override
    public boolean removeItem(int id) {
        if (id < 1) {
            return false;
        }

        TI32Result result = _tokenDao.update(
            "DELETE FROM UserToken WHERE id=?",
            id
        );
        if (result.getError() == ZErrorDef.FAIL || result.getValue() < 1) {
            return false;
        }

        return true;
    }

    @Override
    public boolean updateItem(int id, Pair... pairs) {
        if (id < 1 || pairs.length < 1) {
            return false;
        }

        Object[] objects = new Object[pairs.length + 1];
        StringBuilder sb = new StringBuilder("UPDATE UserToken SET ");
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

        TI32Result result = _tokenDao.update(sb.toString(), objects);
        if (result.getError() == ZErrorDef.FAIL || result.getValue() < 1) {
            return false;
        }

        return true;
    }
}
