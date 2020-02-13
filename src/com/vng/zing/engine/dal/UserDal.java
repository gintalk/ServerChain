/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import java.util.HashMap;
import java.util.List;

import com.vng.zing.engine.sql.dao.UserDao;
import com.vng.zing.engine.sql.exception.ZException;
import com.vng.zing.engine.type.Pair;

/**
 *
 * @author namnh16
 */
public class UserDal implements BaseDal {
//    private static final Logger LOGGER = ZLogger.getLogger(UserDal.class);

    private final UserDao _userDao;
    public static final UserDal INSTANCE = new UserDal();

    private UserDal() {
        _userDao = new UserDao(APPLICATION_DB_INST);
    }

    @Override
    public HashMap<String, Object> getItemAsMap(int id) throws ZException {
        if (id < 1) {
            return null;
        }

        List<HashMap<String, Object>> rows = _userDao.selectAsListMap("SELECT id, name, type, joinDate FROM User WHERE id=?",
            id
        );
        if (rows != null) {
            return rows.get(0);
        }

        return null;
    }

    @Override
    public HashMap<String, Object> getItemAsMap(String name) throws ZException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int addItemAutoKey(Object... params) throws ZException {
        if (params == null || params.length < 4) {
            return 0;
        }

        return _userDao.insert(
            "INSERT INTO User VALUES(?,?,?,?)",
            true,
            params
        );
    }

    @Override
    public boolean addItem(Object... params) throws ZException {
        if (params == null || params.length < 4) {
            return false;
        }

        return _userDao.insert(
            "INSERT INTO User VALUES(?,?,?,?)",
            false,
            params
        ) > 0;
    }

    @Override
    public boolean removeItem(int i) throws ZException {
        throw new UnsupportedOperationException("Can only cascade row removal from UserToken");
    }

    @Override
    public boolean updateItem(int id, Pair... pairs) throws ZException {
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

        return _userDao.update(sb.toString(), objects);
    }
}
