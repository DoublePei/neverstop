package com.ns.net.common.model.enums;

public enum  FileFromType {
    FROM_JOB(0), FROM_INPUT(1), FROM_UPLOAD(2), UNKNOWN(-1);

    private int code;

    FileFromType(int code) {
        this.code = code;
    }

    public static FileFromType of(String code) {
        return of(Integer.parseInt(code));
    }

    public static FileFromType of(int code) {
        for (FileFromType fileFromType : FileFromType.values()) {
            if (fileFromType.code == code) {
                return fileFromType;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return this.code;
    }
}
