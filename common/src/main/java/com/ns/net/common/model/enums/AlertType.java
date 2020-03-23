package com.ns.net.common.model.enums;

public enum AlertType {

    SMS(1), WECHAT(2), EMAIL(3);

    private int code;

    AlertType(int code) {
        this.code = code;
    }

    public static AlertType of(String code) {
        return of(Integer.parseInt(code));
    }

    public static AlertType of(int code) {
        for (AlertType alertType : AlertType.values()) {
            if (alertType.code == code) {
                return alertType;
            }
        }
        throw new IllegalArgumentException("unsupported alert type " + code);
    }

    public int getCode() {
        return this.code;
    }
}