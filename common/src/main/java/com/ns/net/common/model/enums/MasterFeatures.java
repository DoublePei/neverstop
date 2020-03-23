package com.ns.net.common.model.enums;

public enum MasterFeatures {

    SCHEDULE(1, "schedule"),
    PAUSE(2, "pause"),
    RUN(3, "run"),
    RERUN(4, "rerun"),
    KILLED(5, "kill"),
    RESUME(6, "resume"),
    DELETE(7, "delete");

    private Integer code;
    private String desc;

    MasterFeatures(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MasterFeatures getTaskType(int code) {
        for (MasterFeatures taskState : MasterFeatures.values()) {
            if (taskState.code == code) {
                return taskState;
            }
        }
        throw new IllegalArgumentException("unsupported task state " + code);
    }

    public int getCode() {
        return this.code;
    }
    public String getDesc() {
        return this.desc;
    }

    public static String getDesc(int code) {
        for (MasterFeatures taskState : MasterFeatures.values()) {
            if (taskState.code == code) {
                return taskState.desc;
            }
        }
        throw new IllegalArgumentException("unsupported task state " + code);
    }
    }
