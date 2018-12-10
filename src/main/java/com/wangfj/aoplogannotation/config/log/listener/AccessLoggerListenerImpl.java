package com.wangfj.aoplogannotation.config.log.listener;

import com.wangfj.aoplogannotation.config.log.object.AccessLoggerInfo;
import org.springframework.stereotype.Component;

/**
 * @author wangfujie
 * @date 2018-12-07 15:44
 * @description 记录日志实现类
 */
@Component
public class AccessLoggerListenerImpl implements AccessLoggerListener {
    /**
     * 当产生访问日志时,将调用此方法.注意,此方法内的操作应尽量设置为异步操作,否则可能影响请求性能
     *
     * @param loggerInfo 产生的日志信息
     */
    @Override
    public void onLogger(AccessLoggerInfo loggerInfo) {
        System.out.println("自定义日志注解成功！");
    }
}
