/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.sql.dao;

import com.vng.zing.common.ZErrorDef;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.engine.sql.exception.ZExceptionHandler;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.media.common.thrift.TI32Result;
import com.vng.zing.media.common.utils.CommonUtils;
import com.vng.zing.resource.thrift.TZException;

/**
 *
 * @author namnh16
 */
public abstract class MySqlDao {

    private static final Logger LOGGER = ZLogger.getLogger(MySqlDao.class);
    private final String INSTANCE_NAME;
    private static final HashMap<String, BasicDataSource> DATA_SOURCES = new HashMap<>();

    public MySqlDao(String instanceName) {
        INSTANCE_NAME = instanceName;
    }

    private BasicDataSource getDataSource() throws SQLException {
        if (DATA_SOURCES.containsKey(INSTANCE_NAME)) {
            return DATA_SOURCES.get(INSTANCE_NAME);
        }

        synchronized (DATA_SOURCES) {
            BasicDataSource dataSource = new BasicDataSource();

//            try {
                dataSource.setDriverClassName(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.driverClassName", ""));
                dataSource.setUrl(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.url", ""));
                dataSource.setUsername(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.username", ""));
                dataSource.setPassword(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.password", ""));
                dataSource.setValidationQuery(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.validationQuery", "SELECT 1"));
                dataSource.setConnectionInitSqls(Arrays.asList("set names utf8mb4"));

                DATA_SOURCES.put(INSTANCE_NAME, dataSource);
//            } catch (Exception ex) {
//                throw new ZExceptionHandler(ex);
//            }

            return dataSource;
        }
    }

    private Connection getConnection() throws SQLException {
//        try {
            return getDataSource().getConnection();
//        } catch (SQLException ex) {
//            throw new ZExceptionHandler(ex);
//        }
    }

    public TI32Result insert(String sql, boolean returnAutoKey, Object... params){
        TI32Result result = new TI32Result(ZErrorDef.FAIL);
        try (
            Connection connection = getConnection();
            PreparedStatement ps = (returnAutoKey)
                ? connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
                : connection.prepareStatement(sql)) {
            if (!CommonUtils.isEmpty(params)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            int nRows = ps.executeUpdate();
            result.setFieldValue(result.fieldForId(1), ZErrorDef.SUCCESS);
            if (nRows > 0) {
                if (returnAutoKey) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            result.setFieldValue(result.fieldForId(2), rs.getInt(1));
                        }
                        else{
                            result.setFieldValue(result.fieldForId(3), "ResultSet empty: auto-generated key expected");
                        }
                    }
                } else {
                    result.setFieldValue(result.fieldForId(2), nRows);
                }
            }
            else{
                result.setFieldValue(result.fieldForId(3), "Insert failed: 0 row effected");
            }
        } catch (SQLException ex) {
            result.setFieldValue(result.fieldForId(1), ZErrorDef.FAIL);
            result.setFieldValue(result.fieldForId(3), ex.getMessage());
        }

        return result;
    }

    public TI32Result update(String sql, Object... params){
        TI32Result result = new TI32Result(ZErrorDef.FAIL);
        try (
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            if (!CommonUtils.isEmpty(params)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            int nRows = ps.executeUpdate();
            result.setFieldValue(result.fieldForId(1), ZErrorDef.SUCCESS);
            if(nRows > 0){
                result.setFieldValue(result.fieldForId(2), nRows);
            }
            else{
                result.setFieldValue(result.fieldForId(3) , "Update failed: 0 row affected");
            }
        } catch (SQLException ex) {
            result.setFieldValue(result.fieldForId(1), ZErrorDef.FAIL);
            result.setFieldValue(result.fieldForId(3), ex.getMessage());
        }

        return result;
    }

    public List<HashMap<String, Object>> selectAsListMap(String sql, Object... params) throws TZException {
        List<HashMap<String, Object>> result = null;
        try (
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            if (!CommonUtils.isEmpty(params)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                result = deserializeAsListMap(rs);
            }
        } catch (SQLException ex) {
            TZException tzex = new TZException();
            ZExceptionHandler.INSTANCE.prepareException(tzex, ex);
            throw tzex;
        }

        return result;
    }

    private List<HashMap<String, Object>> deserializeAsListMap(ResultSet rs) throws SQLException{
        List<HashMap<String, Object>> result = null;

//        try {
            if (rs != null && rs.isBeforeFirst()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int nCols = rsmd.getColumnCount();

                List<String> cols = new ArrayList<>();
                for (int i = 0; i < nCols; i++) {
                    cols.add(rsmd.getColumnName(i + 1));      // MySQL column index starts from 1, not 0
                }

                result = new ArrayList<>();
                while (rs.next()) {
                    HashMap<String, Object> row = new HashMap<>();
                    for (String col : cols) {
                        row.put(col, rs.getObject(col));
                    }

                    result.add(row);
                }
            }
//        } catch (SQLException ex) {
//            throw new ZExceptionHandler(ex);
//        }

        return result;
    }
}
