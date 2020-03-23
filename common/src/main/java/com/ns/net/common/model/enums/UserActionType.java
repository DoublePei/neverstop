package com.ns.net.common.model.enums;

public enum UserActionType {
    CREATE(1, "创建"),
    DELETE(2, "删除"),
    UPDATE(3, "修改"),
    VIEW(4, "查看"),
    ONLINE(5, "上线"),
    OFFLINE(6, "下线"),
    RUN(7, "运行"),
    RERUN(8, "重跑"),
    COMPLEMENT(9, "补数"),
    KILL(10, "补数"),
    MARK_SUCCESS(10, "补数"),
    SHUTDOWN(10, "停发"),
    FREE_DEPENDENCIES(11, "解除依赖");

    private int code;
    private String desc;

    UserActionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserActionType of(int code) {
        for(UserActionType actionType : UserActionType.values()) {
            if(actionType.code == code) {
                return actionType;
            }
        }
        throw new IllegalArgumentException("unsupported action type " + code);
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
