/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.engine.sql.dao;

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
import com.vng.zing.engine.sql.exception.ZException;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.media.common.utils.CommonUtils;

/**
 *
 * @author namnh16
 * @param <T>
 */
public abstract class MySqlDao<T> {

    private static final Logger LOGGER = ZLogger.getLogger(MySqlDao.class);
    private final String INSTANCE_NAME;
    private static final HashMap<String, BasicDataSource> DATA_SOURCES = new HashMap<>();

    public MySqlDao(String instanceName) {
        INSTANCE_NAME = instanceName;
    }

    private BasicDataSource getDataSource() throws ZException {
        if (DATA_SOURCES.containsKey(INSTANCE_NAME)) {
            return DATA_SOURCES.get(INSTANCE_NAME);
        }

        synchronized (DATA_SOURCES) {
            BasicDataSource dataSource = new BasicDataSource();

            try {
                dataSource.setDriverClassName(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.driverClassName", ""));
                dataSource.setUrl(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.url", ""));
                dataSource.setUsername(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.username", ""));
                dataSource.setPassword(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.password", ""));
                dataSource.setValidationQuery(ZConfig.Instance.getString(MySqlDao.class, INSTANCE_NAME, "jdbc.validationQuery", "SELECT 1"));
                dataSource.setConnectionInitSqls(Arrays.asList("set names utf8mb4"));

                DATA_SOURCES.put(INSTANCE_NAME, dataSource);
            } catch (Exception ex) {
                throw new ZException(ex);
            }

            return dataSource;
        }
    }

    private Connection getConnection() throws ZException {
        try {
            return getDataSource().getConnection();
        } catch (SQLException ex) {
            throw new ZException(ex);
        }
    }

//    private void closeConnection(Connection connection) throws ZException {
//        try{
//            if (connection != null) {
//                connection.close();
//            }
//        }
//        catch(SQLException ex){
//            throw new ZException(ex);
//        }
//    }
//
//    private void closeConnection(ResultSet resultSet) throws ZException {
//        try{
//            if (resultSet != null && resultSet.getStatement() != null && resultSet.getStatement().getConnection() != null) {
//                resultSet.getStatement().getConnection().close();
//            }
//        }
//        catch(SQLException ex){
//            throw new ZException(ex);
//        }
//    }
//
//    private PreparedStatement getPreparedStatement(String sql) throws ZException {
//        try{
//            return getConnection().prepareStatement(sql);
//        }
//        catch(SQLException ex){
//            throw new ZException(ex);
//        }
//    }
    public int insert(String sql, boolean returnAutoKey, Object... params) throws ZException {
        int result = 0;
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
            if (nRows > 0) {
                if (returnAutoKey) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            result = rs.getInt(1);
                        }
                    }
                } else {
                    result = nRows;
                }
            }
        } catch (SQLException ex) {
            throw new ZException(ex);
        }

        return result;
    }

    public boolean update(String sql, Object... params) throws ZException {
        boolean result = false;
        try (
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            if (!CommonUtils.isEmpty(params)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            int nRows = ps.executeUpdate();
            result = nRows > 0;
        } catch (SQLException ex) {
            throw new ZException(ex);
        }

        return result;
    }

    public List<HashMap<String, Object>> selectAsListMap(String sql, Object... params) throws ZException {
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
            throw new ZException(ex);
        }

        return result;
    }

    private List<HashMap<String, Object>> deserializeAsListMap(ResultSet rs) throws ZException {
        List<HashMap<String, Object>> result = null;

        try {
            if (rs != null && rs.isBeforeFirst()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int nCols = rsmd.getColumnCount();

                List<String> cols = new ArrayList<>();
                for (int i = 0; i < nCols; i++) {
                    cols.add(rsmd.getColumnName(i + 1));      // column index should start from 1 and not 0
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
        } catch (SQLException ex) {
            throw new ZException(ex);
        }

        return result;
    }
}
