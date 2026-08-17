package com.lezijie.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lezijie.dao.UserDao;
import com.lezijie.po.User;
import com.lezijie.vo.ResultInfo;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 用户业务逻辑层
 * @since 2026-08-17
 */
public class UserService {

    private UserDao userDao = new UserDao();

    /**
     * 用户登录
     * @param userName 用户名
     * @param userPwd 密码
     * @return 返回结果集
     */
    public ResultInfo<User> userLogin(String userName, String userPwd) {
        ResultInfo<User> resultInfo = new ResultInfo<>();

        // 数据回显：当登录失败时，将登录信息返回给页面显示
        User u = new User();
        u.setUname(userName);
        u.setUpwd(userPwd);
        resultInfo.setResult(u);

        // 判断参数是否为空
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(userPwd)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户姓名或密码不能为空！");
            // 返回ResultInfo对象
            return resultInfo;
        }

        // 如果不为空，通过用户名查询用户对象
        User user = userDao.queryUserByName(userName);

        // 判断用户对象是否为空
        if (user == null) {
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在！");
            return resultInfo;
        }

        // 将前台传递的密码按照MD5算法的方式加密
        userPwd = DigestUtil.md5Hex(userPwd);
        // 判断加密后的密码是否与数据库的一致
        if (!userPwd.equals(user.getUpwd())) {
            // 如果密码不正确
            resultInfo.setCode(0);
            resultInfo.setMsg("用户密码不正确！");
            return resultInfo;
        }

        resultInfo.setCode(1);
        resultInfo.setResult(user);
        return resultInfo;
    }
}
