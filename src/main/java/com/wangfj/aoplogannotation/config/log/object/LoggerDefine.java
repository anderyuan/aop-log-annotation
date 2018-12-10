package com.wangfj.aoplogannotation.config.log.object;

/**
 * @author wangfujie
 * @date 2018-12-06 15:41
 * @description 封装注解的参数
 */
public class LoggerDefine {

    private String module;

    private String describe;

    private Integer type;

    public LoggerDefine(String module, String describe, Integer type) {
        this.module = module;
        this.describe = describe;
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
