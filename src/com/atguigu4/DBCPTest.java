package com.atguigu4;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPTest {
    /*
    测试DBCP的数据库连接池技术
     */
    //方式一：
    @Test
    public void testGetConnection() throws SQLException {
        //创建了DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("xwj0922");
        //还可以设置其他涉及数据库的数据
        source.setInitialSize(10);

        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    //方式二：使用配置文件(推荐）
    @Test
    public void testGetConnection1() throws Exception {
        Properties pros = new Properties();
        //方式一：
        //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        BasicDataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);

    }


}
