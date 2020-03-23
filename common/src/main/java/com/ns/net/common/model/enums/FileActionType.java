package com.ns.net.common.model.enums;

public enum FileActionType {
    CREATE(1), UPDATE(2), DELETE(3);

    private int code;

    FileActionType(int code) {
        this.code = code;
    }

    public static FileActionType of(String code) {
        return of(Integer.parseInt(code));
    }

    public static FileActionType of(int code) {
        for (FileActionType fileActionType : FileActionType.values()) {
            if (fileActionType.code == code) {
                return fileActionType;
            }
        }
        throw new IllegalArgumentException("unsupported upload file action type " + code);
    }

    public int getCode() {
        return this.code;
    }
}
