package com.wangfj.aoplogannotation.controller;

import com.wangfj.aoplogannotation.config.annotation.AccessLogger;
import com.wangfj.aoplogannotation.utils.constant.SysLogType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangfujie
 * @date 2018-12-06 15:41
 * @description 测试的controller
 */
@RestController
@RequestMapping("/log")
public class TestAopLogController {

    @RequestMapping("/test")
    @AccessLogger(module = "记录模块" , describe = "记录的描述" , type = SysLogType.REQUEST)
    public String test(String param){
        System.out.println("参数：" + param);
        return "测试记录日志，返回内容";
    }
}
