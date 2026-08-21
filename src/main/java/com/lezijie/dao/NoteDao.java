package com.lezijie.dao;


import cn.hutool.core.util.StrUtil;
import com.lezijie.po.Note;
import com.lezijie.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 云记数据访问层
 * @since 2026-08-21
 */
public class NoteDao {
    /**
     * 添加或修改云记，返回修改的行数
     * @param note
     * @return
     */
    public int addOrUpdate(Note note) {
        String sql = "";
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        // 判断noteId是否为空，如果为空则为添加，不为空则为修改
        if (note.getNoteId() == null) {
            sql = "insert into tb_note (typeId, title, content, pubTime) values (?,?,?,now())";
        } else {
            sql = "update tb_note set typeId = ?, title = ?, content = ? where noteId = ?";
            params.add(note.getNoteId());
        }
        return BaseDao.executeUpdate(sql,params);
    }

    /**
     * 查询当前登录用户的云记数量，返回总记录数
     * @param userId
     * @return
     */
    public long findNoteCount(Integer userId, String title, String date, String typeId) {
        // 定义SQL语句
        String sql = "select count(1) from tb_note n inner join tb_note_type t on n.typeId = t.typeId where userId = ?";
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 判断条件查询的参数是否为空（如果查询的参数不为空，则拼接sql语句，并设置所需要的参数）
        if (!StrUtil.isBlank(title)) {
            // 如果查询的参数不为空，则拼接sql语句，并设置所需要的参数
            sql += " and title like concat('%',?,'%') ";
            params.add(title);
        } else if (!StrUtil.isBlank(date)) {
            sql += " and date_format(pubTime,'%Y年%m月') = ? ";
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId = ? ";
            params.add(typeId);
        }

        return (long) BaseDao.findSingleValue(sql,params);
    }

    /**
     * 查询当前登录用户下当前页的数据列表，返回note集合
     * @param userId
     * @param index
     * @param pageSize
     * @return
     */
    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize, String title, String date, String typeId) {
        String sql = "select noteId, title, pubTime from tb_note n inner join tb_note_type t on n.typeId = t.typeId where userId = ?";

        List<Object> params = new ArrayList<>();
        params.add(userId);
        // 判断条件查询的参数是否为空（如果查询的参数不为空，则拼接sql语句，并设置所需要的参数）
        if (!StrUtil.isBlank(title)) {
            // 如果查询的参数不为空，则拼接sql语句，并设置所需要的参数
            sql += " and title like concat('%',?,'%') ";
            params.add(title);
        } else if (!StrUtil.isBlank(date)) {
            sql += " and date_format(pubTime,'%Y年%m月') = ? ";
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId = ? ";
            params.add(typeId);
        }

        // 拼接分页的SQL语句
        sql +=  " order by pubTime desc limit ?,?";

        params.add(index);
        params.add(pageSize);


        return (List<Note>) BaseDao.queryRows(sql, params, Note.class);
    }

    public List<NoteVo> findNoteCountByDate(Integer userId) {
        String sql = "SELECT COUNT(1) noteCount,DATE_FORMAT(pubTime,'%Y年%m月') groupName FROM tb_note n " +
                " INNER JOIN tb_note_type t " +
                " on n.typeId = t.typeId " +
                " WHERE userId=? " +
                " GROUP BY DATE_FORMAT(pubTime,'%Y年%m月') " +
                " ORDER BY DATE_FORMAT(pubTime,'%Y年%m月') DESC";
        List<Object> params = new ArrayList<>();
        params.add(userId);

        return (List<NoteVo>) BaseDao.queryRows(sql, params, NoteVo.class);
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        // 定义SQL语句
        String sql = "select count(noteId) noteCount, t.typeId, t.typeName groupName from tb_note n " +
                "right join tb_note_type t on n.typeId = t.typeId where userId = ? " +
                "GROUP BY t.typeId order by count(noteId) DESC";

        List<Object> params = new ArrayList<>();
        params.add(userId);

        return (List<NoteVo>) BaseDao.queryRows(sql, params, NoteVo.class);
    }

    /**
     * 通过id查询云记对象
     * @return
     */
    public Note findNoteById(String noteId) {
        // 定义SQL
        String sql = "select noteId, title, content, pubTime, typeName, n.typeId from tb_note n " +
                "inner join tb_note_type t " +
                "on n.typeId = t.typeId where noteId = ?";
        List<Object> params = new ArrayList<>();
        params.add(noteId);

        return (Note) BaseDao.queryRow(sql, params, Note.class);
    }

    /**
     * 通过noteId删除云记记录，返回受影响的行数
     * @param noteId
     * @return
     */
    public int deleteNoteById(String noteId) {
        String sql = "delete from tb_note where noteId = ?";

        List<Object> params = new ArrayList<>();
        params.add(noteId);
        return BaseDao.executeUpdate(sql,params);
    }
}
