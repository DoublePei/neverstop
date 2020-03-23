package com.ns.net.manager;

import com.ns.net.common.model.bo.SchedulerJobBo;
import com.ns.net.config.QuartzConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 提供了添加quartzJob和remove的功能
 * 调度接收到调度的任务后提交给TaskScheduler进行调度
 */
@Slf4j
@Component
public class QuartzScheduler implements com.ns.net.scheduler.Scheduler {

    /**
     * 启动时初始化一个scheduler
     */
    private Scheduler scheduler;

    @Autowired
    private QuartzConfig quartzConfig;

    /**
     * 使用 cronScheduler
     *
     * @param job
     * @param jobClass
     */
    public void addJob(SchedulerJobBo job, Class<? extends Job> jobClass) {
        TriggerKey triggerKey = job.getTriggerKey();
        try {
            if (scheduler.checkExists(triggerKey)) {
                scheduler.rescheduleJob(triggerKey, job.getCronTrigger());
                return;
            }
            scheduler.scheduleJob(job.getJobDetail(jobClass), job.getCronTrigger());
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    public void removeJob(SchedulerJobBo job) {
        if (job == null) {
            return;
        }
        TriggerKey triggerKey = job.getTriggerKey();
        try {
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(job.getJobKey());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void start() {
        try {
            scheduler = new StdSchedulerFactory(quartzConfig).getScheduler();
            scheduler.start();
            log.info("QuartzScheduler  started ...");
        } catch (SchedulerException e) {
            log.error("start QuartzScheduler failed ...");
        }
    }

    @Override
    public void stop() {
        try {
            if (scheduler == null || scheduler.isShutdown()) {
                return;
            }
            scheduler.shutdown();
            log.info("QuartzScheduler  stopped ...");
        } catch (SchedulerException e) {
            log.error("shutting down QuartzScheduler failed ...");
        }
    }

    public void addJob(SchedulerJobBo basicSchedulerJobBo) {
        addJob(basicSchedulerJobBo, QuartzJob.class);
    }
}
