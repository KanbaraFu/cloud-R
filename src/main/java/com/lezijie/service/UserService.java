package com.lezijie.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lezijie.dao.UserDao;
import com.lezijie.po.User;
import com.lezijie.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

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

    /**
     * 验证昵称的唯一性
     * @param nick
     * @param userId
     * @return
     */
    public Integer checkNick(String nick, Integer userId) {
        if (StrUtil.isBlank(nick)) {
            return 0;
        }

        User user = userDao.queryUserByNickAndUserId(nick, userId);
        if (user != null) {
            return 0;
        }
        return 1;
    }

    /**
     * 修改用户信息
     * @param req
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest req) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        String nick = req.getParameter("nick");
        String mood = req.getParameter("mood");
        if (StrUtil.isBlank(nick)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }

        User user = (User) req.getSession().getAttribute("user");
        // 设置修改的昵称和心情
        user.setNick(nick);
        user.setMood(mood);
        try {
            Part part = req.getPart("img");
            String header = part.getHeader("Content-Disposition");
            System.out.println(header);
            // 获取具体的请求头对应的值
            String str = header.substring(header.lastIndexOf("=") + 2);
            System.out.println(str);
            // 获取上传的文件名
            String fileName = str.substring(0, str.length() - 1);
            if (!StrUtil.isBlank(fileName)) {
                // 如果用户上传了头像，则更新用户对象中的头像
                user.setHead(fileName);
                String filePath = req.getServletContext().getRealPath("/WEB-INF/upload/");
                part.write(filePath + "/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 调用Dao层的更新方法，返回受影响的行数
        int row = userDao.updateUser(user);

        if (row > 0) {
            resultInfo.setCode(1);
            // 更新session中的用户对象
            req.getSession().setAttribute("user",user);
        } else {
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
