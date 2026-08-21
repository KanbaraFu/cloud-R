package com.lezijie.web;


import cn.hutool.core.util.StrUtil;
import com.lezijie.po.Note;
import com.lezijie.po.NoteType;
import com.lezijie.po.User;
import com.lezijie.service.NoteService;
import com.lezijie.service.NoteTypeService;
import com.lezijie.vo.ResultInfo;

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
 * @description 云记Servlet控制层
 * @since 2026-08-21
 */
@WebServlet("/note")
public class NoteServlet extends HttpServlet {

    private final NoteService noteService = new NoteService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置首页导航的高亮
        req.setAttribute("menu_page","note");
        // 得到用户行为
        String actionName = req.getParameter("actionName");

        if ("view".equals(actionName)) {
            noteView(req, resp);
        } else if ("addOrUpdate".equals(actionName)) {
            // 添加或修改云记
            addOrUpdate(req,resp);
        } else if ("detail".equals(actionName)) {
            // 查询云记详情
            noteDetail(req,resp);
        } else if ("delete".equals(actionName)) {
            // 删除云记详情
            noteDelete(req,resp);
        }
    }

    /**
     * 删除云记
     * @param req
     * @param resp
     */
    private void noteDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String noteId = req.getParameter("noteId");
        Integer code = noteService.deleteNote(noteId);
        resp.getWriter().write(code + "");
        resp.getWriter().close();
    }

    /**
     * 查询云记详情
     * @param req
     * @param resp
     */
    private void noteDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 接收参数（noteId）
        String noteId = req.getParameter("noteId");
        // 2. 调用Service层的查询方法，返回Note对象
        Note note = noteService.findNoteById(noteId);
        // 3. 将Note对象设置到request请求域中
        req.setAttribute("note", note);
        // 4. 设置首页动态包含的页面值
        req.setAttribute("changePage","note/detail.jsp");
        // 5. 请求跳转到index.jsp
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    /**
     * 添加或修改云记
     * @param req
     * @param resp
     */
    private void addOrUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String typeId = req.getParameter("typeId");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        // 如果是修改操作，需要接收noteId
        String noteId = req.getParameter("noteId");

        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId, title, content, noteId);

        if (resultInfo.getCode() == 1) {
            resp.sendRedirect("index");
        } else {
            req.setAttribute("resultInfo",resultInfo);
            String url = "note?actionName=view";
            // 如果是修改操作，需要传递noteId
            if (StrUtil.isBlank(noteId)) {
                url += "&noteId=" + noteId;
            }
            req.getRequestDispatcher(url).forward(req,resp);
        }


    }

    /**
     * 进入发布云记页面
     * @param req
     * @param resp
     */
    private void noteView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 修改操作
        // 得到要修改的云记ID
        String noteId = req.getParameter("noteId");
        // 通过noteId查询云记对象
        Note note = noteService.findNoteById(noteId);
        // 将note对象设置到请求域中
        req.setAttribute("noteInfo",note);

        // 1. 从Session对象中获取用户对象
        User user = (User) req.getSession().getAttribute("user");
        // 2. 通过用户ID查询对应的类型列表
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        // 3. 将类型列表设置到request请求域中
        req.setAttribute("typeList", typeList);
        // 4. 设置首页动态包含的页面值
        req.setAttribute("changePage", "note/view.jsp");
        // 5. 请求转发跳转到index.jsp
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
