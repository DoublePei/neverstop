package com.ns.net.common.model.enums;

public enum JobReleaseState {
    
    OFFLINE(0), ONLINE(1), DELETED(-1);
    
    private int code;
    
    JobReleaseState(int code) {
        this.code = code;
    }
    
    public static JobReleaseState of(int code) {
        for(JobReleaseState state : JobReleaseState.values()) {
            if(state.code == code) {
                return state;
            }
        }
        throw new IllegalArgumentException("unsupported job state " + code);
    }
    
    public int getCode() {
        return this.code;
    }
}
