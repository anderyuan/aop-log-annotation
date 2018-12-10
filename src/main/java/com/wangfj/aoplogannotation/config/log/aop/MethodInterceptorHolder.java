package com.wangfj.aoplogannotation.config.log.aop;

import com.wangfj.aoplogannotation.utils.AopUtils;
import com.wangfj.aoplogannotation.utils.ThreadLocalUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.DigestUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangfujie
 * @date 2018-12-06 15:44
 * @description DESCRIPTION
 */
public class MethodInterceptorHolder {
    /**
     * 参数名称获取器,用于获取方法参数的名称
     */
    public static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private String id;

    private Method method;

    private Object target;

    private Map<String, Object> args;

    public static MethodInterceptorHolder current() {
        return ThreadLocalUtils.get(MethodInterceptorHolder.class.getName());
    }

    public static MethodInterceptorHolder clear() {
        return ThreadLocalUtils.getAndRemove(MethodInterceptorHolder.class.getName());
    }

    public static MethodInterceptorHolder setCurrent(MethodInterceptorHolder holder) {
        return ThreadLocalUtils.put(MethodInterceptorHolder.class.getName(), holder);
    }

    public static MethodInterceptorHolder create(MethodInvocation invocation) {
        String id = DigestUtils.md5DigestAsHex(String.valueOf(invocation.getMethod().hashCode()).getBytes());
        String[] argNames = nameDiscoverer.getParameterNames(invocation.getMethod());
        Object[] args = invocation.getArguments();
        Map<String, Object> argMap = new LinkedHashMap<>();
        for (int i = 0, len = args.length; i < len; i++) {
            argMap.put(argNames[i] == null ? "arg" + i : argNames[i], args[i]);
        }
        return new MethodInterceptorHolder(id,invocation.getMethod(),invocation.getThis(), argMap);
    }

    public MethodInterceptorHolder set() {
        MethodInterceptorHolder.setCurrent(this);
        return this;
    }

    public MethodInterceptorHolder(String id, Method method, Object target, Map<String, Object> args) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(id);
        Objects.requireNonNull(method);
        Objects.requireNonNull(target);
        Objects.requireNonNull(args);
        this.id = id;
        this.method = method;
        this.target = target;
        this.args = args;
    }

    public String getId() {
        return id;
    }

    public Method getMethod() {
        return method;
    }

    public Object getTarget() {
        return target;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public <T extends Annotation> T findMethodAnnotation(Class<T> annClass) {
        return AopUtils.findMethodAnnotation(annClass, method, annClass);
    }

    public <T extends Annotation> T findClassAnnotation(Class<T> annClass) {
        return AopUtils.findAnnotation(target.getClass(), annClass);
    }

    public <T extends Annotation> T findAnnotation(Class<T> annClass) {
        return AopUtils.findAnnotation(target.getClass(), method, annClass);
    }
}
