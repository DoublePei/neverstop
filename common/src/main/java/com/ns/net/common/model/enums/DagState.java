package com.ns.net.common.model.enums;

public enum DagState {

    SUBMITTED(0), PENDING(1), RUNNING(2), COMPLETED(3), STOPPED(4);

    private int code;

    DagState(int code) {
        this.code = code;
    }
    
    public static DagState of(int code) {
        for(DagState batchState : DagState.values()) {
            if(batchState.code == code) {
                return batchState;
            }
        }
        throw new IllegalArgumentException("unsupported batch state type " + code);
    }
    
    public int getCode() {
        return this.code;
    }
}
