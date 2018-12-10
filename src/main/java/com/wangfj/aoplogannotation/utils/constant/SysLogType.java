package com.wangfj.aoplogannotation.utils.constant;

/**
 * @author wangfujie
 * @date 2018-12-06 15:25
 * @description 日志操作分类枚举
 */
public enum SysLogType {

    //1-登录 2-请求 3-添加 4-修改 5-删除 6-上传 7-下载 8-异常
    LOGIN(1),REQUEST(2),ADD(3),EDIT(4),DEL(5),UPLOAD(6),DOWNLOAD(7),EXCEPTION(8);

    private Integer value;

    SysLogType(Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static SysLogType getSysLogType(Integer value){
        SysLogType[] sysLogTypes = SysLogType.values();
        for (SysLogType sysLogType : sysLogTypes){
            if (value.equals(sysLogType.getValue())){
                return sysLogType;
            }
        }
        return null;
    }
}
