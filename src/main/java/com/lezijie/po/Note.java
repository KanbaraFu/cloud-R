package com.lezijie.po;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 云记实体类
 * @since 2026-08-21
 */
@Getter
@Setter
@ToString
public class Note {
    private Integer noteId;
    private String title;
    private String content;
    private Integer typeId;
    private Date pubTime;
    private String typeName;
}
