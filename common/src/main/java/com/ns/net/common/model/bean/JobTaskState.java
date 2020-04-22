package com.ns.net.common.model.bean;

import lombok.Data;

import java.util.Date;

@Data
public class JobTaskState {

    private int id;
    private int jobId;
    private Date scheduleTime;
    private int TaskState;
}
