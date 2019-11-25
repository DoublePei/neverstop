package com.ns.net.manager;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public class QuartzSchedulerTest {

    @Test
    public void main() {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                    .withIdentity(getJobKey())
                    .build();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerKey())
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
                    .build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            log.error("get scheduler failed ");
        }
    }

    private static TriggerKey getTriggerKey() {
        return TriggerKey.triggerKey("a", "a");
    }

    private static JobKey getJobKey() {
        return JobKey.jobKey("a", "a");
    }


    public class QuartzJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            log.info("I am Quartz job and starting the job");
        }

    }
}