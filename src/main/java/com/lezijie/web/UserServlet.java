package com.lezijie.web;


import com.lezijie.po.User;
import com.lezijie.service.UserService;
import com.lezijie.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 用户Servlet控制层
 * @since 2026-08-17
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 接收用户行为
        String actionName = req.getParameter("actionName");
        // 判断用户行为，调用对应方法
        if ("login".equals(actionName)) {
            // 用户登录
            userLogin(req,resp);
        }
    }

    private void userLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取参数
        String userName = req.getParameter("userName");
        String userPwd = req.getParameter("userPwd");

        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);

        if (resultInfo.getCode() == 1) { // 成功
            req.getSession().setAttribute("user", resultInfo.getResult());
            String rem = req.getParameter("rem");
            Cookie cookie;
            if ("1".equals(rem)) {
                cookie = new Cookie("user", userName + "-" + userPwd);
                cookie.setMaxAge(3*24*60*60);
            } else {
                // 选择0则表示不勾选"记住我选项"，需要删除cookie
                cookie = new Cookie("user", null);
                cookie.setMaxAge(0);
            }
            resp.addCookie(cookie);

            resp.sendRedirect("index.jsp");
        } else { // 失败
            req.setAttribute("resultInfo", resultInfo);
            req.getRequestDispatcher("login.jsp").forward(req ,resp);
        }
    }
}
