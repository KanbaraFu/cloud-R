package com.lezijie;


import com.lezijie.dao.BaseDao;
import com.lezijie.dao.UserDao;
import com.lezijie.po.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 测试用户
 * @since 2026-08-17
 */
public class TestUser {

    @Test
    public void testQueryUserByName() {
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }

    @Test
    public void testAdd() {
        String sql = "insert into tb_user (uname, upwd, nick, head, mood) values (?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add("lisi");
        params.add("e10adc3949ba59abbe56e057f20f883e");
        params.add("lisi");
        params.add("404.jpg");
        params.add("hello");
        int row = BaseDao.executeUpdate(sql, params);
        System.out.println(row);
    }
}
