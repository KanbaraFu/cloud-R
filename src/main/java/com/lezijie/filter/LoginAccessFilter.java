package com.lezijie.filter;


import com.lezijie.po.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 登录过滤拦截器
 * @since 2026-08-18
 */
@WebFilter("/*")
public class LoginAccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 基于HTTP
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 得到访问的路径
        String path = req.getRequestURI(); // 格式：项目路径/资源路径
        if (path.contains("/login.jsp")) {
            chain.doFilter(req,resp);
            return;
        }
        if (path.contains("/statics")) {
            chain.doFilter(req,resp);
            return;
        }
        if (path.contains("/user")) {
            String actionName = req.getParameter("actionName");
            if ("login".equals(actionName)) {
                chain.doFilter(req,resp);
                return;
            }
            chain.doFilter(req,resp);
            return;
        }

        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            chain.doFilter(req,resp);
            return;
        }

        // 免登录
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    String[] val = value.split("-");
                    String userName = val[0];
                    String userPwd = val[1];
                    String url = req.getContextPath() + "/user?actionName=login&rem=1&userName=" + userName + "&userPwd=" + userPwd;
                    resp.sendRedirect(url); // 这里最好不要用请求转发，而是用重定向，否则?后面的值可能传不进去
                    return;
                }
            }
        }

        // 拦截请求
        resp.sendRedirect("login.jsp");
    }

    @Override
    public void destroy() {
    }
}
