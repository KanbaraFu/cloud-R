package com.lezijie.dao;


import com.lezijie.po.NoteType;
import com.lezijie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 类型数据访问层
 * @since 2026-08-19
 */
public class NoteTypeDao {
    /**
     * 通过用户ID查询类型集合
     * @param userId
     * @return
     */
    public List<NoteType> findTypeListByUserId(Integer userId) {

        String sql = "select typeId, typeName, userId from tb_note_type where userId = ?";
        List<Object> params = new ArrayList<>();
        params.add(userId);

        return (List<NoteType>) BaseDao.queryRows(sql, params, NoteType.class);
    }

    /**
     * 通过类型ID查询云记记录的数量，返回云记数量
     * @param typeId
     * @return
     */
    public long findNoteCountByTypeId(String typeId) {
        String sql = "select count(1) from tb_note where typeId = ?";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        return (long) BaseDao.findSingleValue(sql,params);
    }

    /**
     * 通过类型ID删除指定的类型记录，返回受影响的行数
     * @param typeId
     * @return
     */
    public int deleteTypeById(String typeId) {
        String sql = "delete from tb_note_type where typeId = ?";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        return BaseDao.executeUpdate(sql, params);
    }

    /**
     * 查询当前登录用户下，类型名称是否唯一
     * @param typeName
     * @param userId
     * @param typeId
     * @return
     */
    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        String sql = "select * from tb_note_type where userID = ? and typeName = ?";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(typeName);
        NoteType noteType = (NoteType) BaseDao.queryRow(sql, params, NoteType.class);
        // 如果对象为空，则表示无重复的类型名称
        if (noteType == null) {
            return 1;
        } else {
            if (typeId == null) {
                return 0;
            }
            // 跳到这里则有可能代表的是修改操作，又或者是新增类型时遇到了同样的类型名称。
            if (typeId.equals(noteType.getTypeId().toString())) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 添加方法，返回主键
     * @param typeName
     * @param userId
     * @return
     */
    public Integer addType(String typeName, Integer userId) {
        Integer key = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "insert into tb_note_type (typeName,userId) values (?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, typeName);
            preparedStatement.setInt(2, userId);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                // 返回主键的结果集
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    key = resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(resultSet, preparedStatement, connection);
        }
        return key;
    }

    /**
     * 修改方法，返回受影响的行数
     * @param typeName
     * @param typeId
     * @return
     */
    public Integer updateType(String typeName, String typeId) {
        String sql = "update tb_note_type set typeName = ? where typeId = ?";
        List<Object> params = new ArrayList<>();
        params.add(typeName);
        params.add(typeId);
        return BaseDao.executeUpdate(sql,params);
    }
}
