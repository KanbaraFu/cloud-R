package com.lezijie.dao;


import com.lezijie.po.User;
import com.lezijie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 用户数据访问层
 * @since 2026-08-17
 */
public class UserDao {

    public User queryUserByName(String userName) {
        String sql = "select * from tb_user where uname = ?";

        List<Object> params = new ArrayList<>();
        params.add(userName);

        // 调用BaseDao的查询方法
        return (User) BaseDao.queryRow(sql, params, User.class);
    }

    public User queryUserByName02(String userName) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 获取数据库连接
            connection = DBUtil.getConnection();
            String sql = "select * from tb_user where uname = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUname(userName);
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("nick"));
                user.setUpwd(resultSet.getString("upwd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            DBUtil.close(resultSet,preparedStatement,connection);
        }
        return user;
    }
}
