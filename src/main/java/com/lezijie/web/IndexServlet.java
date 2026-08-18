package com.lezijie.web;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 主页web
 * @since 2026-08-18
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置首页导航高亮
        req.setAttribute("menu_page","index");
        // 设置首页动态包含的页面
        req.setAttribute("changePage","note/list.jsp");
        // 请求转发到index.jsp
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
