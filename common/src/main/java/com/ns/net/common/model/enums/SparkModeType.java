package com.ns.net.common.model.enums;

public enum SparkModeType {

    OVERWRITE(1, "overwrite"), APPEND(2, "append");

    private int code;
    private String desc;

    SparkModeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getDesc() {
        return this.desc;
    }
    public int getCode() {
        return this.code;
    }

}