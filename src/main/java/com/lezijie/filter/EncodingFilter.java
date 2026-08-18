package com.lezijie.filter;


import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 字符编码过滤器
 * @since 2026-08-18
 */
@WebFilter("/*") // 过滤所有资源
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 基于HTTP
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 处理POST请求（只针对POST请求）
        req.setCharacterEncoding("UTF-8");

        // 得到请求类型
        String method = req.getMethod();
        // 如果是GET请求，则判断服务器版本
        if ("GET".equalsIgnoreCase(method)) {
            // 得到服务器版本
            String serverInfo = req.getServletContext().getServerInfo(); // Apache Tomcat/7.x
            String version = serverInfo.substring(serverInfo.lastIndexOf("/") + 1,serverInfo.indexOf("."));
            // 判断服务器版本是否是Tomcat7及以下
            if (Integer.parseInt(version) < 8) {
                // Tomcat7及以下版本的服务器的GET请求
                MyWapper myRequest = new MyWapper(req);
                chain.doFilter(myRequest,response);
                return;
            }
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }

    /**
     * 1. 定义内部类（类的本质是request对象）
     * 2. HttpServletRequestWrapper继承包装类
     * 3. 重写getParameter()方法
     */
    class MyWapper extends HttpServletRequestWrapper {

        // 定义成员变量 HttpServletRequest对象（提升构造器中request对象的作用域）
        private HttpServletRequest request;

        /**
         * Constructs a request object wrapping the given request.
         * 可以得到需要处理的reqest对象
         * @param request the {@link HttpServletRequest} to be wrapped.
         * @throws IllegalArgumentException if the request is null
         */
        public MyWapper(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        /**
         * 重写getParameter方法，处理乱码问题（如果重写之后，Tomcat7调用的就是这个request对象）
         * @param name a <code>String</code> specifying the name of the parameter
         *
         * @return
         */
        @Override
        public String getParameter(String name) {
            // 获取参数（乱码的参数值）
            String value = request.getParameter(name);
            if (StrUtil.isBlank(value)) {
                return value;
            }
            // 通过new String()处理乱码
            try {
                value = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }
    }
}
