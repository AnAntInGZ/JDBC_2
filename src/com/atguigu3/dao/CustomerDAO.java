package com.atguigu3.dao;

import com.atguigu2.bean.Customer;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/*
此接口用于规范针对customers表的常用操作
 */
public interface CustomerDAO {
    //将cust对象添加到数据库中
    void insert(Connection conn, Customer cust);
    //针对指定的id，删除表中的一条记录
    void deleteById(Connection conn,int id);
    //针对于cust对象，修改customer内的记录
    void update(Connection conn,Customer cust);
    //根据指定的id查找对应的customer得到对应的customer对象
    Customer getCustomerById(Connection conn,int id);
    //查询customer表中的所有记录
    List<Customer> getAll(Connection conn);
    //返回数据表中的数据的条目数
    Long getCount(Connection conn);
    //最大生日
    Date getMaxBirth(Connection conn);
}
