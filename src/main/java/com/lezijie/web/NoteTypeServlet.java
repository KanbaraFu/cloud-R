package com.lezijie.web;


import com.alibaba.fastjson.JSON;
import com.lezijie.po.NoteType;
import com.lezijie.po.User;
import com.lezijie.service.NoteTypeService;
import com.lezijie.util.JSONUtil;
import com.lezijie.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 类型Servlet控制层
 * @since 2026-08-19
 */
@WebServlet("/type")
public class NoteTypeServlet extends HttpServlet {
    private final NoteTypeService typeService = new NoteTypeService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置首页导航的高亮
        req.setAttribute("menu_page", "type");

        // 得到用户行为
        String actionName = req.getParameter("actionName");
        if ("list".equals(actionName)) {
            typeList(req,resp);
        } else if ("delete".equals(actionName)) {
            deleteType(req,resp);
        } else if ("addOrUpdate".equals(actionName)) {
            // 添加或修改类型
            addOrUpdate(req, resp);
        }
    }

    /**
     * 添加或修改类型
     * @param req
     * @param resp
     */
    private void addOrUpdate(HttpServletRequest req, HttpServletResponse resp) {
        String typeName = req.getParameter("typeName");
        String typeId = req.getParameter("typeId");
        User user = (User) req.getSession().getAttribute("user");
        ResultInfo<Integer> resultInfo = typeService.addOrUpdate(typeName, user.getUserId(), typeId);
        System.out.println(resultInfo);
        JSONUtil.toJSON(resp,resultInfo);
    }

    /**
     * 删除类型
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeId = req.getParameter("typeId");
        ResultInfo<NoteType> resultInfo = typeService.deleteType(typeId);
        JSONUtil.toJSON(resp,resultInfo);
    }

    /**
     * 查询类型列表
     * @param req
     * @param resp
     */
    private void typeList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<NoteType> typeList = typeService.findTypeList(user.getUserId());
        req.setAttribute("typeList",typeList);
        req.setAttribute("changePage", "type/list.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
