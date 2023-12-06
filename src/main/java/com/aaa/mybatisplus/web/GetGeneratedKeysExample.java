package com.aaa.mybatisplus.web;

/**
 * @author liuzhen.tian
 * @version 1.0 TestJdbc.java  2023/11/23 22:50
 */

import java.sql.*;
public class GetGeneratedKeysExample {
    public static void main(String[] args) throws ClassNotFoundException {
        String url = "jdbc:p6spy:mysql://127.0.0.1:3301/master?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true";
        String username = "root";
        String password = "123456";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO dept (dept_name, dept_no) VALUES (?, ?)";

            // 创建 PreparedStatement 对象，并设置参数和标志位
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "1");
            statement.setString(2, "2");

            // 执行插入操作
            statement.executeUpdate();

            // 获取生成的主键值
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1); // 获取第一列的值，即生成的主键值
                System.out.println("Generated ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
