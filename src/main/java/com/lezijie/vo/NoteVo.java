package com.lezijie.vo;


import lombok.Data;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 分组信息
 * @since 2026-08-21
 */
@Data
public class NoteVo {
    private String groupName;
    private long noteCount;

    private Integer typeId;
}
