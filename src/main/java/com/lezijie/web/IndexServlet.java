package com.lezijie.web;


import com.lezijie.po.Note;
import com.lezijie.vo.NoteVo;
import com.lezijie.po.User;
import com.lezijie.service.NoteService;
import com.lezijie.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        // 将用户行为设置到request作用域中（分页导航中需要获取
        //得到用户行为（判断是什么条件查询：标题查询、日期查询、类型查询）
        String actionName = req.getParameter("actionName");
        req.setAttribute("action",actionName);
        if ("searchTitle".equals(actionName)) {
            // 得到查询条件：标题
            String title = req.getParameter("title");
            // 将查询条件设置到req请求域中（查询条件的回显）
            req.setAttribute("title", title);
            noteList(req, resp, title, null, null);
        } else if ("searchDate".equals(actionName)) {
            // 得到查询条件：日期
            String date = req.getParameter("date");
            // 将查询条件设置到req请求域中（查询条件的回显）
            req.setAttribute("date", date);
            // 日期搜索
            noteList(req, resp, null, date,null);
        } else if ("searchType".equals(actionName)) {
            // 得到查询条件：日期
            String typeId = req.getParameter("typeId");
            // 将查询条件设置到req请求域中（查询条件的回显）
            req.setAttribute("typeId", typeId);
            // 日期搜索
            noteList(req, resp, null, null, typeId);
        } else {
            // 分页查询云记列表
            noteList(req, resp, null, null, null);
        }

        // 设置首页动态包含的页面
        req.setAttribute("changePage","note/list.jsp");
        // 请求转发到index.jsp
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    /**
     * 分页查询云记列表
     * @param req
     * @param resp
     * @param title
     */
    private void noteList(HttpServletRequest req, HttpServletResponse resp, String title, String date, String typeId) {
        // 1. 接收参数（当前页、每页显示的数量）
        String pageNum = req.getParameter("pageNum");
        String pageSize = req.getParameter("pageSize");

        // 2. 获取Session作用域中的user对象
        User user = (User) req.getSession().getAttribute("user");

        // 3. 调用Service层查询方法，返回Page对象
        Page<Note> page = new NoteService().findNoteListByPage(pageNum, pageSize, user.getUserId(), title, date, typeId);
        // 4. 将Page对象设置到request作用域中
        req.setAttribute("page", page);

        // 通过日期分组查询当前登录用户下的云记数量
        List<NoteVo> dateInfo = new NoteService().findNoteCountByDate(user.getUserId());
        req.getSession().setAttribute("dateInfo", dateInfo);

        List<NoteVo> typeInfo = new NoteService().findNoteCountByType(user.getUserId());
        req.getSession().setAttribute("typeInfo", typeInfo);
    }
}
