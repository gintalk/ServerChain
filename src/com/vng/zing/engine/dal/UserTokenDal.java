/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.dal;

import com.vng.zing.common.ZErrorDef;
import java.util.HashMap;
import java.util.List;

import com.vng.zing.engine.sql.dao.UserTokenDao;
import com.vng.zing.engine.sql.exception.ZExceptionHandler;
import com.vng.zing.engine.type.Pair;
import com.vng.zing.media.common.thrift.TI32Result;
import com.vng.zing.resource.thrift.TZException;

/**
 *
 * @author namnh16
 */
public class UserTokenDal implements BaseDal {
//    private static final Logger LOGGER = ZLogger.getLogger(UserTokenDal.class);

    private final UserTokenDao _tokenDao;
    public static final UserTokenDal INSTANCE = new UserTokenDal();

    private UserTokenDal() {
        _tokenDao = new UserTokenDao(AUTHENTICATE_DB_INST);
    }

    @Override
    public HashMap<String, Object> getItemAsMap(int id) throws TZException {
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
    public HashMap<String, Object> getItemAsMap(String username) throws TZException {
        if (username == null || username.length() < 1) {
            return null;
        }

        List<HashMap<String, Object>> rows = _tokenDao.selectAsListMap(
            "SELECT id, username, password FROM UserToken WHERE username=?",
            username
        );
        if (rows != null) {
            return rows.get(0);
        }

        return null;
    }

    @Override
    public int addItemAutoKey(Object... params) throws TZException {
        if (params == null || params.length < 2) {
            return 0;
        }

        TI32Result result = _tokenDao.insert(
            "INSERT INTO UserToken(username, password) VALUES(?,?)",
            true,
            params
        );
        if((int) result.getFieldValue(result.fieldForId(1)) == ZErrorDef.FAIL){
            TZException tzex = new TZException();
            ZExceptionHandler.INSTANCE.prepareException(
                tzex,
                (String) result.getFieldValue(result.fieldForId(3)),
                ZExceptionHandler.State.SQL
            );
            throw tzex;
        }
        
        return (int) result.getFieldValue(result.fieldForId(2));
    }

    @Override
    public boolean addItem(Object... params) throws TZException {
        if (params == null || params.length < 2) {
            return false;
        }
        
        TI32Result result = _tokenDao.insert(
            "INSERT INTO UserToken(username, password) VALUES(?,?)",
            false,
            params
        );
        if((int) result.getFieldValue(result.fieldForId(1)) == ZErrorDef.FAIL){
            TZException tzex = new TZException();
            ZExceptionHandler.INSTANCE.prepareException(
                tzex,
                (String) result.getFieldValue(result.fieldForId(3)),
                ZExceptionHandler.State.SQL
            );
            throw tzex;
        }
        
        return (int) result.getFieldValue(result.fieldForId(2)) > 0;
    }

    @Override
    public boolean removeItem(int id) throws TZException {
        if(id < 1){
            return false;
        }
        
        TI32Result result = _tokenDao.update(
            "DELETE FROM UserToken WHERE id=?",
            id
        );
        if((int) result.getFieldValue(result.fieldForId(1)) == ZErrorDef.FAIL){
            TZException tzex = new TZException();
            ZExceptionHandler.INSTANCE.prepareException(
                tzex,
                (String) result.getFieldValue(result.fieldForId(3)),
                ZExceptionHandler.State.SQL
            );
            throw tzex;
        }
        
        return (int) result.getFieldValue(result.fieldForId(2)) > 0;
    }

    @Override
    public boolean updateItem(int id, Pair... pairs) throws TZException {
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
        if((int) result.getFieldValue(result.fieldForId(1)) == ZErrorDef.FAIL){
            TZException tzex = new TZException();
            ZExceptionHandler.INSTANCE.prepareException(
                tzex,
                (String) result.getFieldValue(result.fieldForId(3)),
                ZExceptionHandler.State.SQL
            );
            throw tzex;
        }
        
        return (int) result.getFieldValue(result.fieldForId(2)) > 0;
    }
}
