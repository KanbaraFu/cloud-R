package com.lezijie.service;


import cn.hutool.core.util.StrUtil;
import com.lezijie.dao.NoteDao;
import com.lezijie.po.Note;
import com.lezijie.vo.NoteVo;
import com.lezijie.util.Page;
import com.lezijie.vo.ResultInfo;

import java.util.List;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 云记业务逻辑层
 * @since 2026-08-21
 */
public class NoteService {

    private final NoteDao noteDao = new NoteDao();

    /**
     * 添加或修改云记
     * @param typeId
     * @param title
     * @param content
     * @return
     */
    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content, String noteId) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();

        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择云记类型");
            return resultInfo;
        }
        if (StrUtil.isBlank(title)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记标题不能为空");
            return resultInfo;
        }if (StrUtil.isBlank(content)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记内容不能为空");
            return resultInfo;
        }

        // 设置回显对象
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(Integer.parseInt(typeId));
        // 判断云记id是否为空
        if (!StrUtil.isBlank(noteId)) {
            note.setNoteId(Integer.parseInt(noteId));
        }
        resultInfo.setResult(note);

        int row = noteDao.addOrUpdate(note);

        if (row > 0) {
            resultInfo.setCode(1);
        } else {
            resultInfo.setCode(0);
            resultInfo.setResult(note);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;
    }

    /**
     * 分页查询云记列表
     * @param pageNumStr
     * @param pageSizeStr
     * @param userId
     * @param title
     * @return
     */
    public Page<Note> findNoteListByPage(String pageNumStr, String pageSizeStr, Integer userId,
                                         String title, String date, String typeId) {
        // 设置分页参数的默认值
        Integer pageNum = 1;        // 默认当前页是第一页
        Integer pageSize = 5;      // 默认每页显示10条
        // 1. 参数的非空校验（如果分页参数为空，则设置默认值）
        if (!StrUtil.isBlank(pageNumStr)) {
            // 设置当前页
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (!StrUtil.isBlank(pageSizeStr)) {
            // 设置每页显示的数量
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 2. 查询当前登录用户的云记数量，返回总记录数（long类型）
        long count = noteDao.findNoteCount(userId, title, date, typeId);
        // 3. 判断总记录数是否大于0
        if (count < 1) {
            return null;
        }

        // 4. 如果总记录数大于0，调用Page类的带参构造，得到其他分页参数的值，返回Page对象
        Page<Note> page = new Page<>(pageNum, pageSize, count);

        // 得到数据库中分页查询的开始下标
        Integer index = (pageNum - 1) * pageSize;
        // 5. 查询当前登录用户下当前页的数据列表，返回note集合
        List<Note> noteList = noteDao.findNoteListByPage(userId, index, pageSize, title, date, typeId);
        // 6. 将note集合设置到page对象中
        page.setDataList(noteList);
        // 7. 返回Page对象
        return page;
    }

    /**
     * 通过日期分组查询当前登录用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByDate(Integer userId) {
        return noteDao.findNoteCountByDate(userId);
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        return noteDao.findNoteCountByType(userId);
    }

    /**
     * 查询云记详情
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        // 1. 参数的非空判断
        if (StrUtil.isBlank(noteId)) {
            return null;
        }
        // 2. 调用Dao层的查询，通过noteId查询note对象
        // 3. 返回note对象
        return noteDao.findNoteById(noteId);
    }

    /**
     * 删除云记
     * @param noteId
     * @return
     */
    public Integer deleteNote(String noteId) {
        if (StrUtil.isBlank(noteId)) {
            return 0;
        }
        int row = noteDao.deleteNoteById(noteId);
        if (row > 0) {
            return 1;
        }
        return 0;
    }
}
