package com.lezijie.service;


import cn.hutool.core.util.StrUtil;
import com.lezijie.dao.NoteTypeDao;
import com.lezijie.po.NoteType;
import com.lezijie.vo.ResultInfo;

import java.util.List;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 类型业务逻辑层
 * @since 2026-08-19
 */
public class NoteTypeService {

    private final NoteTypeDao typeDao = new NoteTypeDao();

    public List<NoteType> findTypeList(Integer userId) {
        return typeDao.findTypeListByUserId(userId);
    }

    /**
     * 删除类型
     * @param typeId
     * @return
     */
    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();
        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常。请重试！");
            return resultInfo;
        }

        long noteCount = typeDao.findNoteCountByTypeId(typeId);
        if (noteCount > 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("该类型存在子记录，不可删除");
            return resultInfo;
        }

        int row = typeDao.deleteTypeById(typeId);
        if (row > 0) {
            resultInfo.setCode(1);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败");
        }
        return resultInfo;
    }

    /**
     * 添加或修改类型
     * @param typeName
     * @param userId
     * @param typeId
     * @return
     */
    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId, String typeId) {
        ResultInfo<Integer> resultInfo = new ResultInfo<>();
        if (StrUtil.isBlank(typeName)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不能为空！");
            return resultInfo;
        }

        Integer code = typeDao.checkTypeName(typeName, userId, typeId);
        if (code == 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，请重新输入！");
            return resultInfo;
        }

        Integer key = null; // 主键或受影响的行数
        if (StrUtil.isBlank(typeId)) { // 根据是否有typeId的值来决定是添加还是更新
            key = typeDao.addType(typeName, userId);
        } else {
            key = typeDao.updateType(typeName, typeId);
        }

        if (key > 0) {
            resultInfo.setCode(1);
            resultInfo.setResult(key);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }
        return resultInfo;
    }
}
