package com.atguigu2.dao;

import com.atguigu1.util.JDBCUtils;

import java.io.ObjectStreamException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
封装了针对于数据表的通用的操作
 */
public abstract class BaseDAO<T>{
    private Class<T> clazz = null;
    {
        //获取当前子类的父类的泛型值
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;

        Type[] typeArguments = paramType.getActualTypeArguments();//获取了父类的泛型参数
        clazz = (Class<T>) typeArguments[0];//泛型的第一个参数


    }
    //增删改
    public int update(Connection conn, String sql, Object ...args){
        PreparedStatement ps = null;
        try {
            //2.预编译sql语句，返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符,sql中占位符的个数与可变形参的长度相同！
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1,args[i]);
            }

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //修改其为自动提交数据
            //针对于数据库连接池的使用

            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(null,ps);
        }
            return 0;
    }

    //查询一条
    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object...args){

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {


            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //给cust对象指定的columnName属性赋值为value，通过反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;

    }
    //查询多条记录
    public <T> List<T> getForList(Connection conn,Class<T> clazz, String sql, Object ...args){

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {


            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            //创建集合对象,给t对象指定的属性赋值
            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //给cust对象指定的columnName属性赋值为value，通过反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
    //对于查询特殊值
    public <E> E getValue(Connection conn, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i =0 ;i< args.length;i++){
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                return (E) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }
}
