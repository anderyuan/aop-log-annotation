package com.wangfj.aoplogannotation.config.log.parser;

import com.wangfj.aoplogannotation.config.annotation.AccessLogger;
import com.wangfj.aoplogannotation.config.log.aop.MethodInterceptorHolder;
import com.wangfj.aoplogannotation.config.log.object.LoggerDefine;
import org.springframework.core.annotation.AnnotationUtils;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author wangfujie
 * @date 2018-12-06 15:38
 * @description
 */
public class DefaultAccessLoggerParser implements AccessLoggerParser {

    @Override
    public boolean support(Class clazz, Method method) {
        AccessLogger ann = AnnotationUtils.findAnnotation(method, AccessLogger.class);
        //注解了并且未取消
        return null != ann && !ann.ignore();
    }

    @Override
    public LoggerDefine parse(MethodInterceptorHolder holder) {
        AccessLogger methodAnn = holder.findMethodAnnotation(AccessLogger.class);
        AccessLogger classAnn = holder.findClassAnnotation(AccessLogger.class);
        String action = Stream.of(classAnn, methodAnn)
                .filter(Objects::nonNull)
                .map(AccessLogger::module)
                .reduce((c, m) -> c.concat("-").concat(m))
                .orElse("");
        String describe = Stream.of(classAnn, methodAnn)
                .filter(Objects::nonNull)
                .map(AccessLogger::describe)
                .flatMap(Stream::of)
                .reduce((c, s) -> c.concat("\n").concat(s))
                .orElse("");

        Integer type = null;
        if (null != methodAnn){
            type = methodAnn.type().getValue();
        }
        return new LoggerDefine(action,describe,type);
    }
}
