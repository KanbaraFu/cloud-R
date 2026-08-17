package com.lezijie.po;


import lombok.Getter;
import lombok.Setter;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 用户实体类
 * @since 2026-08-17
 */
@Getter
@Setter

public class User {
    private Integer userId; // 用户ID
    private String uname;   // 用户名称
    private String upwd;    // 用户密码
    private String nick;    // 用户昵称
    private String head;    // 用户头像
    private String mood;    // 用户签名

}
