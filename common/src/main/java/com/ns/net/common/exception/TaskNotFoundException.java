package com.ns.net.common.exception;


public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long taskId) {
        super("Scheduler task " + taskId + " not found");
    }
}
