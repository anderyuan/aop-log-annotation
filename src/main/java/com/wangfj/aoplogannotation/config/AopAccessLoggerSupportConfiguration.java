package com.wangfj.aoplogannotation.config;

import com.wangfj.aoplogannotation.config.log.aop.AopAccessLoggerSupport;
import com.wangfj.aoplogannotation.config.log.listener.AccessLoggerListener;
import com.wangfj.aoplogannotation.config.log.parser.AccessLoggerParser;
import com.wangfj.aoplogannotation.config.log.parser.DefaultAccessLoggerParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangfujie
 * @date 2018-12-10 13:19
 * @description AOP 访问日志记录自动配置
 */
@ConditionalOnClass(AccessLoggerListener.class)
@Configuration
public class AopAccessLoggerSupportConfiguration {

    @Bean
    @ConditionalOnMissingBean(AopAccessLoggerSupport.class)
    public AopAccessLoggerSupport aopAccessLoggerSupport() {
        return new AopAccessLoggerSupport();
    }

    @Bean
    @ConditionalOnMissingBean(AccessLoggerParser.class)
    public AccessLoggerParser defaultAccessLoggerParser() {
        return new DefaultAccessLoggerParser();
    }

    @Bean
    @ConditionalOnMissingBean(ListenerProcessor.class)
    public ListenerProcessor listenerProcessor(AopAccessLoggerSupport aopAccessLoggerSupport) {
        return new ListenerProcessor(aopAccessLoggerSupport);
    }

    public static class ListenerProcessor implements BeanPostProcessor {

        private final AopAccessLoggerSupport aopAccessLoggerSupport;

        public ListenerProcessor(AopAccessLoggerSupport aopAccessLoggerSupport) {
            this.aopAccessLoggerSupport = aopAccessLoggerSupport;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof AccessLoggerListener) {
                aopAccessLoggerSupport.addListener(((AccessLoggerListener) bean));
            }
            if (bean instanceof AccessLoggerParser) {
                aopAccessLoggerSupport.addParser(((AccessLoggerParser) bean));
            }
            return bean;
        }
    }
}
