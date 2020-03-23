package com.ns.net.common.model.enums;

public enum JobDependence {

    SELFDENPENDUCE(1), NOTSELFDENPENDUCE(0);

    private int code;

    JobDependence(int code) {
        this.code = code;
    }

    public static JobDependence of(int code) {
        for (JobDependence batchState : JobDependence.values()) {
            if (batchState.code == code) {
                return batchState;
            }
        }
        throw new IllegalArgumentException("unsupported batch state type " + code);
    }

    public int getCode() {
        return this.code;
    }
}
