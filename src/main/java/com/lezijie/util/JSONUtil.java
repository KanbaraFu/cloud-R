package com.lezijie.util;


import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description JSON工具类，专门将对象转换成JSON格式的字符串，响应给ajax的回调函数
 * @since 2026-08-19
 */
public class JSONUtil {

    public static void toJSON(HttpServletResponse resp, Object result) {
        // 设置响应类型以及编码格式(JSON类型)
        resp.setContentType("applicaton/json;charset=UTF-8");
        // 得到字符输出流，使用try-with-resources关闭资源
        try (PrintWriter out = resp.getWriter()) {
            // 通过fastjson的方法，将ResultInfo对象转换成JSON格式的字符串
            String json = JSON.toJSONString(result);
            out.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
