package com.wangfj.aoplogannotation.config.annotation;

import com.wangfj.aoplogannotation.utils.constant.SysLogType;
import com.wangfj.aoplogannotation.config.log.object.AccessLoggerInfo;
import java.lang.annotation.*;

/**
 * @author wangfujie
 * @date 2018-12-06 14:54
 * @description 浏览记录的日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLogger {

    /**
     * @return 模块说明
     * @see AccessLoggerInfo#getAction()
     */
    String module();

    /**
     * @return 对类或方法的详细描述
     * @see AccessLoggerInfo#getDescribe()
     */
    String describe() default "";

    /**
     * 日志类型 1-登录 2-请求 3-添加 4-修改 5-删除 6-上传 7-下载 8-异常
     *
     * @return
     */
    SysLogType type() default SysLogType.REQUEST;

    /**
     * @return 是否取消日志记录, 如果不想记录某些方法或者类, 设置为true即可
     */
    boolean ignore() default false;

}
