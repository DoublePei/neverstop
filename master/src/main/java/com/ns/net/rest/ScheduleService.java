package com.ns.net.rest;

import com.ns.net.common.model.bo.SchedulerJobBo;
import com.ns.net.manager.QuartzScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private QuartzScheduler quartzScheduler;

    public void scheduleJob(Long id) {
        quartzScheduler.addJob(SchedulerJobBo.getBasicSchedulerJobBo());
    }
}
