package com.lezijie.po;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 类型实体类
 * @since 2026-08-19
 */
@Getter
@Setter
@ToString
public class NoteType {
    private Integer typeId;
    private String typeName;
    private Integer userId;
}
