package com.wangfj.aoplogannotation.config.log.parser;

import com.wangfj.aoplogannotation.config.log.aop.MethodInterceptorHolder;
import com.wangfj.aoplogannotation.config.log.object.LoggerDefine;
import java.lang.reflect.Method;

/**
 * @author wangfujie
 * @date 2018-12-06 15:37
 * @description 注解解剖器接口
 */
public interface AccessLoggerParser {
    /**
     * 判断注解是否启用支持的方法
     * @param clazz
     * @param method
     * @return
     */
    boolean support(Class clazz, Method method);

    /**
     * 格式化注解参数处理
     * @param holder
     * @return
     */
    LoggerDefine parse(MethodInterceptorHolder holder);
}
