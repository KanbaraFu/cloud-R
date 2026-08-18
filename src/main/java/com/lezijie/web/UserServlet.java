package com.lezijie.web;


import com.lezijie.po.User;
import com.lezijie.service.UserService;
import com.lezijie.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 用户Servlet控制层
 * @since 2026-08-17
 */
@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置首页导航高亮
        req.setAttribute("menu_page","user");
        // 接收用户行为
        String actionName = req.getParameter("actionName");
        // 判断用户行为，调用对应方法
        if ("login".equals(actionName)) {
            // 用户登录
            userLogin(req,resp);
        } else if ("logout".equals(actionName)) {
            userLogout(req,resp);
        } else if ("userCenter".equals(actionName)) {
            userCenter(req,resp);
        } else if ("userHead".equals(actionName)) {
            userHead(req,resp);
        } else if ("checkNick".equals(actionName)) {
            checkNick(req,resp);
        } else if ("updateUser".equals(actionName)) {
            updateUser(req,resp);
        }
    }

    /**
     * 修改用户信息
     * @param req
     * @param resp
     */
    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultInfo<User> resultInfo = userService.updateUser(req);
        req.setAttribute("resultInfo", resultInfo);
        req.getRequestDispatcher("user?actionName=userCenter").forward(req,resp);
    }

    /**
     * 验证昵称的唯一性
     * @param req
     * @param resp
     */
    private void checkNick(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nick = req.getParameter("nick");
        User user = (User) req.getSession().getAttribute("user");
        Integer code = userService.checkNick(nick, user.getUserId());
        resp.getWriter().write(code + "");
        resp.getWriter().close();
    }

    /**
     * 加载头像
     * @param req
     * @param resp
     */
    private void userHead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String head = req.getParameter("imageName");
        String realPath = req.getServletContext().getRealPath("/WEB-INF/upload/");
        File file = new File(realPath + "/" + head);
        // 通过截取得到图片的后缀
        String pic = head.substring(head.lastIndexOf(".") + 1);
        if ("PNG".equalsIgnoreCase(pic)) {
            resp.setContentType("image/png");
        } else if ("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)) {
            resp.setContentType("image/jpeg");
        } else if ("GIF".equalsIgnoreCase(pic)) {
            resp.setContentType("image/gif");
        }

        FileUtils.copyFile(file,resp.getOutputStream());
    }

    /**
     * 进入个人中心
     * 1. 设置首页动态包含的页面值
     * 2. 请求转发到index
     * @param req
     * @param resp
     */
    private void userCenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("changePage","user/info.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    /**
     * 用户退出
     * @param req
     * @param resp
     */
    private void userLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        resp.sendRedirect("login.jsp");
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
