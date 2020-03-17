package com.ns.net.common.model.bo;


import com.ns.net.common.model.enums.JobCycle;
import com.ns.net.common.model.enums.JobPriority;
import com.ns.net.common.model.enums.JobReleaseState;
import com.ns.net.common.model.enums.JobType;
import com.ns.net.common.model.mapper.SchedulerJob;
import lombok.Data;
import lombok.experimental.Accessors;
import org.quartz.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Splitter.on;
import static java.lang.Long.valueOf;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.quartz.CronScheduleBuilder.cronSchedule;

@Data
@Accessors(chain = true)
public class SchedulerJobBo implements Serializable {
    private static final long serialVersionUID = -2117547527224199953L;

    public static final String DATA_KEY = "data";
    public static final String DEFAULT_TRIGGER_GROUP = "ns";
    public static final String DEFAULT_JOB_GROUP = "ns";

    private Long id;
    private String jobName;
    private JobType jobType;
    private JobPriority jobPriority;
    private String creatorId;
    private List<String> owners;
    private List<String> alertUsers;
    private List<Long> alertTypes;
    private String scheduleCron;
    private Boolean isSelfDependent;
    private JobCycle jobCycle;
    private Integer maxRetryTimes;
    private Long retryInterval;
    private Long executionTimeout;
    private List<String> workerGroups;
    private JobReleaseState jobReleaseState;
    private String description;
    private String jobConfiguration;
    private String ossPath;
    private List<SchedulerJobBo> dependencies = new ArrayList<>();

    public static SchedulerJobBo getBasicSchedulerJobBo() {
        return new SchedulerJobBo()
                .setJobName("test")
                .setId(1L)
                .setJobType(JobType.SHELL)
                .setJobPriority(JobPriority.HIGH)
                .setScheduleCron("* * * * * ?");
    }

    /**
     * 实体类转Business类
     *
     * @param schedulerJob
     * @return
     */
    public static SchedulerJobBo from(SchedulerJob schedulerJob) {
        if (null == schedulerJob)
            return null;

        return new SchedulerJobBo().setId(schedulerJob.getId())
                .setJobName(schedulerJob.getJobName())
                .setCreatorId(schedulerJob.getCreatorId())
                .setJobType(schedulerJob.getJobType() == null ? null
                        : JobType.of(schedulerJob.getJobType()))
                .setJobPriority(schedulerJob.getJobPriority() == null ? null
                        : JobPriority.of(schedulerJob.getJobPriority()))
                .setJobReleaseState(schedulerJob.getJobReleaseState() == null ? null
                        : JobReleaseState.of(schedulerJob.getJobReleaseState()))
                .setOwners(schedulerJob.getOwnerIds() == null ? null :
                        on(",").splitToList(schedulerJob.getOwnerIds()))
                .setAlertUsers(schedulerJob.getAlertUsers() == null ? null :
                        on(",").splitToList(schedulerJob.getAlertUsers()))
                .setAlertTypes(schedulerJob.getAlertIds() == null ? null :
                        on(",").splitToList(schedulerJob.getAlertIds())
                                .stream()
                                .map(i -> valueOf(i))
                                .collect(toList()))
                .setIsSelfDependent(schedulerJob.getIsSelfDependent() == null ? null
                        : toBoolean(schedulerJob.getIsSelfDependent()))
                .setScheduleCron(schedulerJob.getScheduleCron())
                .setMaxRetryTimes(schedulerJob.getMaxRetryTimes())
                .setRetryInterval(schedulerJob.getRetryInterval())
                .setExecutionTimeout(schedulerJob.getExecutionTimeout())
                .setWorkerGroups(schedulerJob.getListOfWorkerGroups())
                .setDescription(schedulerJob.getDescription())
                .setJobConfiguration(schedulerJob.getJobConfiguration())
                .setOssPath(schedulerJob.getOssPath());

    }

    public TriggerKey getTriggerKey() {
        return TriggerKey.triggerKey(quartzKey(), DEFAULT_TRIGGER_GROUP);
    }

    public JobKey getJobKey() {
        return JobKey.jobKey(quartzKey(), DEFAULT_TRIGGER_GROUP);
    }

    private String quartzKey() {
        return this.jobName + "_" + this.id;
    }

    public CronTrigger getCronTrigger() {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(getTriggerKey())
                .startNow()
                .withSchedule(cronSchedule(this.scheduleCron).withMisfireHandlingInstructionIgnoreMisfires())
                .build();

    }

    public JobDetail getJobDetail(Class<? extends Job> jobClass) {
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(getJobKey())
                .usingJobData(getJobData()).build();
    }

    private JobDataMap getJobData() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(DATA_KEY, this);
        return jobDataMap;
    }
}