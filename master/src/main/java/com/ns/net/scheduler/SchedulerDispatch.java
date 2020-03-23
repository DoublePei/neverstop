package com.ns.net.scheduler;

import com.ns.net.common.model.bo.SchedulerTaskBo;
import com.ns.net.common.model.interfaces.Scheduler;

public interface SchedulerDispatch extends Scheduler {
    SchedulerTaskBo submit(SchedulerTaskBo task);
    boolean kill(SchedulerTaskBo task);
}
