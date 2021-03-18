package com.atguigu5.dbutils;

import com.atguigu2.bean.Customer;
import com.atguigu4.utils.JDBCUtils;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryRunnerTest {

    @Test
    public void testInsert() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";

            int insertCount = runner.update(conn,sql,"蔡徐坤","caixukun@126.com","1997-09-21");
            System.out.println(insertCount);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            com.atguigu1.util.JDBCUtils.closeResource(conn,null);

        }
    }
    //查询测试
    //BeanHandler：封装表中的一条记录
    @Test
    public void testQuery1(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql ="select id,name,email,birth from customers where id = ?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 23);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            com.atguigu1.util.JDBCUtils.closeResource(conn,null);

        }

    }
    //查询多条记录
    @Test
    public void testQuery2(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql ="select id,name,email,birth from customers where id < ?";
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> customers = runner.query(conn, sql, handler, 23);
            customers.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            com.atguigu1.util.JDBCUtils.closeResource(conn,null);
        }
    }
    //maphandler:对应表中的一条记录，将字段及相应字段的值作为map中的key和value
    @Test
    public void testQuery3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 23);

            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.atguigu1.util.JDBCUtils.closeResource(conn, null);

        }
    }
    //maplisthandler:对应表中的多条记录，将字段及相应字段的值作为map中的key和value，添加到List中
    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id < ?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> query = runner.query(conn, sql, handler, 23);

            query.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.atguigu1.util.JDBCUtils.closeResource(conn, null);

        }
    }
    //ScalarHandler对特殊值的查询
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            Long query = (Long) runner.query(conn, sql, handler);

            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.atguigu1.util.JDBCUtils.closeResource(conn, null);

        }
    }


    //ScalarHandler对特殊值的查询
    @Test
    public void testQuery6() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select max(birth) from customers";
            ScalarHandler handler = new ScalarHandler();
            Date birth = (Date) runner.query(conn, sql, handler);

            System.out.println(birth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.atguigu1.util.JDBCUtils.closeResource(conn, null);

        }
    }
    //自定义ResultSetHandler的实现类
    @Test
    public void testQuery7() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>(){

                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {

                    return null;
                }
            };
            Customer customer = runner.query(conn, sql, handler, 23);

            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            com.atguigu1.util.JDBCUtils.closeResource(conn, null);

        }
    }
}
