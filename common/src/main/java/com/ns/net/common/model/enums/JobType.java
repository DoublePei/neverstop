package com.ns.net.common.model.enums;

public enum JobType {

    MYSQL2HIVE(1), HIVE2MYSQL(2), HQL(3), SHELL(4), PYTHON(5), SPARK(6), ALGOMODEL(7);

    private int code;

    private JobType(int code) {
        this.code = code;
    }

    public static JobType of(int code) {
        for (JobType jobType : JobType.values()) {
            if (jobType.code == code) {
                return jobType;
            }
        }
        throw new IllegalArgumentException("unsupported job type " + code);
    }

    public static JobType getJobTypeByName(String name) {
        for (JobType jobType : JobType.values()) {
            if (jobType.toString().equalsIgnoreCase(name)) {
                return jobType;
            }
        }
        throw new IllegalArgumentException("unsupported job name " + name);
    }

    public int getCode() {
        return this.code;
    }
}