package com.wangfj.aoplogannotation.config.log.aop;

import com.wangfj.aoplogannotation.config.annotation.AccessLogger;
import com.wangfj.aoplogannotation.config.log.listener.AccessLoggerListener;
import com.wangfj.aoplogannotation.config.log.object.AccessLoggerInfo;
import com.wangfj.aoplogannotation.config.log.object.LoggerDefine;
import com.wangfj.aoplogannotation.config.log.parser.AccessLoggerParser;
import com.wangfj.aoplogannotation.utils.AopUtils;
import com.wangfj.aoplogannotation.utils.WebUtil;
import com.wangfj.aoplogannotation.utils.constant.SysLogType;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangfujie
 * @date 2018-12-06 15:19
 * @description 使用AOP记录访问日志,并触发{@link AccessLoggerListener#onLogger(AccessLoggerInfo)}
 */
public class AopAccessLoggerSupport extends StaticMethodMatcherPointcutAdvisor {
    private final List<AccessLoggerListener> listeners = new ArrayList<>();

    private final List<AccessLoggerParser> loggerParsers = new ArrayList<>();

    public AopAccessLoggerSupport addListener(AccessLoggerListener loggerListener) {
        listeners.add(loggerListener);
        return this;
    }

    public AopAccessLoggerSupport addParser(AccessLoggerParser parser) {
        loggerParsers.add(parser);
        return this;
    }

    public AopAccessLoggerSupport() {
        setAdvice((MethodInterceptor) methodInvocation -> {
            MethodInterceptorHolder methodInterceptorHolder = MethodInterceptorHolder.create(methodInvocation);
            AccessLoggerInfo info = createLogger(methodInterceptorHolder);
            Object response;
            try {
                listeners.forEach(listener -> listener.onLogBefore(info));
                //响应前
                response = methodInvocation.proceed();
                //响应后
                info.setResponse(response);
                info.setResponseTime(System.currentTimeMillis());
            } catch (Throwable e) {
                info.setException(e);
                info.setAction("异常");
                throw e;
            } finally {
                //触发监听（做业务处理）
                listeners.forEach(listener -> listener.onLogger(info));
            }
            return response;
        });
    }

    protected AccessLoggerInfo createLogger(MethodInterceptorHolder holder) {
        AccessLoggerInfo info = new AccessLoggerInfo();

        info.setRequestTime(System.currentTimeMillis());
        LoggerDefine define = loggerParsers.stream()
                .filter(parser -> parser.support(ClassUtils.getUserClass(holder.getTarget()), holder.getMethod()))
                .findAny()
                .map(parser -> parser.parse(holder))
                .orElse(null);
        if (define != null) {
            info.setModule(define.getModule());
            info.setDescribe(define.getDescribe());
            Integer type = define.getType();
            SysLogType sysLogType = SysLogType.getSysLogType(type);
            switch (sysLogType){
                case LOGIN:
                    info.setAction("登录");
                    info.setActionType(SysLogType.LOGIN.getValue());
                    break;
                case REQUEST:
                    info.setAction("访问操作");
                    info.setActionType(SysLogType.REQUEST.getValue());
                    break;
                case ADD:
                    info.setActionType(SysLogType.ADD.getValue());
                    info.setAction("添加操作");
                    break;
                case EDIT:
                    info.setActionType(SysLogType.EDIT.getValue());
                    info.setAction("修改操作");
                    break;
                case DEL:
                    info.setActionType(SysLogType.DEL.getValue());
                    info.setAction("删除操作");
                    break;
                case UPLOAD:
                    info.setActionType(SysLogType.UPLOAD.getValue());
                    info.setAction("上传");
                    break;
                case DOWNLOAD:
                    info.setActionType(SysLogType.DOWNLOAD.getValue());
                    info.setAction("下载");
                    break;
                case EXCEPTION:
                    info.setActionType(SysLogType.EXCEPTION.getValue());
                    info.setAction("异常");
                    break;
            }
        }
        info.setParameters(holder.getArgs());
        info.setTarget(holder.getTarget().getClass());
        info.setMethod(holder.getMethod());

        HttpServletRequest request = WebUtil.getHttpServletRequest();
        if (null != request) {
            info.setHttpHeaders(WebUtil.getHeaders(request));
            info.setIp(WebUtil.getIpAddr(request));
            info.setHttpMethod(request.getMethod());
            info.setUrl(request.getRequestURL().toString());
        }
        return info;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        AccessLogger ann = AopUtils.findAnnotation(aClass, method, AccessLogger.class);
        if (null == ann) {
            return false;
        }
        if (ann != null && ann.ignore()) {
            return false;
        }
        return true;
    }
}
