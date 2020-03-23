package com.ns.net.manager;

import com.ns.net.common.model.bo.SchedulerJobBo;
import com.ns.net.common.model.bo.SchedulerTaskBo;
import com.ns.net.common.model.enums.TaskTriggerType;
import com.ns.net.common.util.Dates;
import com.ns.net.scheduler.SchedulerDispatch;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.util.Date;

import static com.ns.net.common.model.bo.SchedulerTaskBo.from;

@Slf4j
public class QuartzJob implements Job {

    private static SchedulerDispatch schedulerDispatch;

    public static void init(SchedulerDispatch schedulerDispatch) {
        QuartzJob.schedulerDispatch = schedulerDispatch;
    }

    /**
     * CORN分发任务去执行
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        SchedulerJobBo job = getJob(jobDetail);
        Date scheduledFireTime = context.getScheduledFireTime();
        LocalDateTime localDateTime = Dates.localDateTime(scheduledFireTime);
        SchedulerTaskBo from = from(job);
        from.setTaskTriggerType(TaskTriggerType.CRON)
                .setScheduleTime(localDateTime);
        schedulerDispatch.submit(from);
    }

    public static SchedulerJobBo getJob(JobDetail jobDetail) {
        return (SchedulerJobBo) jobDetail.getJobDataMap().get("data");
    }
}
