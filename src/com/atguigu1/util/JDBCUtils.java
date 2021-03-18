package com.atguigu1.util;

import org.apache.commons.dbutils.DbUtils;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/*
操作数据库的工具类
 */
public class JDBCUtils {

    public static java.sql.Connection getConnection() throws Exception {
        //1.读取配置文件中的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        java.sql.Connection conn = DriverManager.getConnection(url,user,password);
      //  System.out.println(conn);
        return conn;
    }
    //关闭连接和Statement的操作
    public static void closeResource(java.sql.Connection conn, PreparedStatement ps){
        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);

    }
    public static void closeResource(java.sql.Connection conn, PreparedStatement ps, ResultSet resultSet){
        try {
            DbUtils.close(conn);
            DbUtils.close(ps);
            DbUtils.close(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
