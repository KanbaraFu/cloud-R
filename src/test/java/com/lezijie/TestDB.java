package com.lezijie;


import com.lezijie.util.DBUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lezijie.util.DBUtil.getConnection;

/**
 * @author KanbaraFu
 * @version 1.0
 * @description 测试
 * @since 2026-08-17
 */
public class TestDB {

    // 使用日志工厂类，记录日志
    private Logger logger = LoggerFactory.getLogger(TestDB.class);

    /**
     * 单元测试方法
     * 1.方法的返回值，建议使用void，一般没有返回值
     * 2.参数列表，建议空参，一般没有参宿
     * 3.方法上需要设置@Test注解
     * 4.每个方法都能独立运行
     * 判定结果：绿色表示成功，红色表示失败
     */
    @Test
    public void testDB() {
        System.out.println(getConnection());

        // 使用日志
        logger.info("获取数据库连接：{}",DBUtil.getConnection());
    }
}
