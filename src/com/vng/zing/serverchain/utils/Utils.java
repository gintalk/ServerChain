/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.utils;

import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 *
 * @author namnh16
 */
public class Utils {
    private static final String URL_AUTH = "jdbc:mysql://localhost:3306/AuthenticateDB";
    private static final String URL_APP = "jdbc:mysql://localhost:3306/ApplicationDB";
    private static final String USER_DB = "root";
    private static final String PASSWORD_DB = "6264842";
    private static Connection connection;
    
    public static Connection getAuthDBConnection() throws SQLException{
        connection = DriverManager.getConnection(URL_AUTH, USER_DB, PASSWORD_DB);
        
        return connection;
    }
    
    public static Connection getAppDBConnection() throws SQLException{
        connection = DriverManager.getConnection(URL_APP, USER_DB, PASSWORD_DB);
        
        return connection;
    }
    
    public static String getURLAuth(){
        return URL_AUTH;
    }
    
    public static String getURLApp(){
        return URL_APP;
    }
    
    public static String getUserDB(){
        return USER_DB;
    }
    
    public static String getPasswordDB(){
        return PASSWORD_DB;
    }
    
    public static String toStringFromSQLDate(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
    
    public static java.sql.Date getCurrentSQLDate(){
        return new Date(new java.util.Date().getTime());
    }
    
    public static UserType toUserType(String type){
        return UserType.valueOf(type);
    }
    
    public static String toStringFromUserType(UserType type){
        switch (type.getValue()) {
            case 0:
                return "REGULAR";
            case 1:
                return "PREMIUM";
            case 2:
                return "ADMIN";
            default:
                return "UNSUPORRTED";
        }
    }
    
    public static String md5(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest md = MessageDigest.getInstance("md5");
        
        byte[] mdBytes = md.digest(password.getBytes("UTF-8"));
        
        return getString(mdBytes);
    }
    
    private static String getString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        
        for( int i=0; i<bytes.length; i++ ){
            byte b = bytes[ i ];
            String hex = Integer.toHexString((int) 0x00FF & b);
            
            if (hex.length() == 1){
                sb.append("0");
            }
            sb.append( hex );
        }
        
        return sb.toString();
    }
    
    public static User findUserById(int id) throws SQLException{
        Connection conn = Utils.getAppDBConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT * FROM "
            + "User WHERE id=?");
        stm.setInt(1, id);
            
        ResultSet resultSet = stm.executeQuery();
        resultSet.next();
                
        User user = toStruct(resultSet);
                
        return user;
    }
    
    private static User toStruct(ResultSet resultSet) throws SQLException{
        User user = new User();
        
        user.setFieldValue(user.fieldForId(1), resultSet.getInt("id"));
        user.setFieldValue(user.fieldForId(2), resultSet.getString("name"));
        user.setFieldValue(user.fieldForId(3), UserType.valueOf(resultSet.getObject("type").toString()));
        user.setFieldValue(user.fieldForId(4), Utils.toStringFromSQLDate(resultSet.getDate("joinDate")));
        
        return user;
    }
}
