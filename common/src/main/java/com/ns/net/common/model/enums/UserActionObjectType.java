package com.ns.net.common.model.enums;

public enum UserActionObjectType {
    JOB(1, "任务"),
    TASK(2, "实例"),
    RESOURCE(3, "资源文件"),
    DATASOURCE(4, "数据源"),
    WORKER(5, "执行机"),
    FILE(5, "文件");

    private int code;
    private String desc;

    UserActionObjectType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserActionObjectType of(int code) {
        for(UserActionObjectType userActionObjectType : UserActionObjectType.values()) {
            if(userActionObjectType.code == code) {
                return userActionObjectType;
            }
        }
        throw new IllegalArgumentException("unsupported action object type " + code);
    }

    public int getCode() {
        return this.code;
    }
}
