package com.atguigu1.transaction;

import com.atguigu1.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*

 */
public class TransactionTest {

    /*
    针对于用户AA给BB转账
    user_table

    1.什么叫数据库事务？
    事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
    一组逻辑操作单元：一个或多个DML操作

    2.事务处理的原则
    保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
    当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)
    ，那么这些修改就永久地保存下来；要么数据库管理系统将放弃所作的所有修改，
    整个事务回滚(rollback)到最初状态。

    3.数据一旦提交，就不可回滚

    4.哪些操作会导致数据的自动提交？
            >DDL操作一旦执行，都会自动提交
            >DML默认情况下，一旦执行，就会自动提交
                >通过set autocommit = false取消自动提交
            >默认在关闭连接时，会自动地提交数据
     */
    @Test
    public void testUpdate(){

        String sql1="update user_table set balance = balance - 100 where user = ?";
        update(sql1,"AA");

        //模拟网络异常
        System.out.println(10 / 0);

        String sql2="update user_table set balance = balance + 100 where user = ?";
        update(sql2,"BB");

        System.out.println("转账成功");
    }
//通用的增删改操作
    public void update(String sql,Object ...args){
        java.sql.Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符,sql中占位符的个数与可变形参的长度相同！
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1,args[i]);
            }

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(conn,ps);
        }

    }
    //考虑数据库事务后的转账操作

    @Test
    public void testUpdateWithTx(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //取消数据地自动提交功能
            conn.setAutoCommit(false);
            String sql1="update user_table set balance = balance - 100 where user = ?";
            update(conn,sql1,"AA");

            //模拟网络异常
            System.out.println(10 / 0);

            String sql2="update user_table set balance = balance + 100 where user = ?";
            update(conn,sql2,"BB");

            System.out.println("转账成功");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally{
            JDBCUtils.closeResource(conn,null);
        }

    }
    public void update(Connection conn,String sql, Object ...args){
        PreparedStatement ps = null;
        try {
            //2.预编译sql语句，返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符,sql中占位符的个数与可变形参的长度相同！
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1,args[i]);
            }

            ps.execute();
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

    }

}
