package com.ns.net.common.model.mapper;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Splitter.on;
import static java.time.LocalDateTime.now;


@Data
@Accessors(chain = true)
@Table("t_kepler_scheduler_task")
public class SchedulerTask {

    @Id
    private Long id;
    private Long jobId;
    private Integer taskState;
    private String ossPath;
    private String workerGroups;
    private String workerHost;
    private Integer workerPort;
    private String creatorName;
    private String creatorEmail;
    private Integer retryTimes;
    private Integer maxRetryTimes;
    private Long retryInterval;
    private Long executionTimeout;
    private Long pid;
    private String applicationId;
    private Integer taskTriggerType;
    private Integer jobType;
    private Integer isSelfDependent;
    private Integer jobPriority;
    private String scheduleCron;
    private LocalDateTime scheduleTime; //quartz fire time, includes missing fire time
    private LocalDateTime pendingTime; //time of submit to task scheduler
    private LocalDateTime waitingTime; //time of submit to task dispatcher
    private LocalDateTime dispatchedTime; //time of dispatched to worker
    private LocalDateTime startTime; //time of start running
    private LocalDateTime endTime;  //time of success/fail/killed
    private Long elapseTime;    // endTime - startTime, duration of task execution
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public SchedulerTask updateIgnoreNull(SchedulerTask schedulerTask) {
        if (schedulerTask.getId() != null) this.id = schedulerTask.getId();
        if (schedulerTask.getJobId() != null) this.jobId = schedulerTask.getJobId();
        if (schedulerTask.getTaskState() != null) this.taskState = schedulerTask.getTaskState();
        if (schedulerTask.getOssPath() != null) this.ossPath = schedulerTask.getOssPath();
        if (schedulerTask.getWorkerGroups() != null) this.workerGroups = schedulerTask.getWorkerGroups();
        if (schedulerTask.getWorkerHost() != null) this.workerHost = schedulerTask.getWorkerHost();
        if (schedulerTask.getWorkerPort() != null) this.workerPort = schedulerTask.getWorkerPort();
        if (schedulerTask.getCreatorName() != null) this.creatorName = schedulerTask.getCreatorName();
        if (schedulerTask.getCreatorEmail() != null) this.creatorEmail = schedulerTask.getCreatorEmail();
        if (schedulerTask.getRetryTimes() != null) this.retryTimes = schedulerTask.getRetryTimes();
        if (schedulerTask.getMaxRetryTimes() != null) this.maxRetryTimes = schedulerTask.getMaxRetryTimes();
        if (schedulerTask.getRetryInterval() != null) this.retryInterval = schedulerTask.getRetryInterval();
        if (schedulerTask.getExecutionTimeout() != null) this.executionTimeout = schedulerTask.getExecutionTimeout();
        if (schedulerTask.getPid() != null) this.pid = schedulerTask.getPid();
        if (schedulerTask.getApplicationId() != null) this.applicationId = schedulerTask.getApplicationId();
        if (schedulerTask.getElapseTime() != null) this.elapseTime = schedulerTask.getElapseTime();
        if (schedulerTask.getTaskTriggerType() != null) this.taskTriggerType = schedulerTask.getTaskTriggerType();
        if (schedulerTask.getJobType() != null) this.jobType = schedulerTask.getJobType();
        if (schedulerTask.getIsSelfDependent() != null) this.isSelfDependent = schedulerTask.getIsSelfDependent();
        if (schedulerTask.getJobPriority() != null) this.jobPriority = schedulerTask.getJobPriority();
        if (schedulerTask.getScheduleCron() != null) this.scheduleCron = schedulerTask.getScheduleCron();
        if (schedulerTask.getScheduleTime() != null) this.scheduleTime = schedulerTask.getScheduleTime();
        if (schedulerTask.getPendingTime() != null) this.pendingTime = schedulerTask.getPendingTime();
        if (schedulerTask.getWaitingTime() != null) this.waitingTime = schedulerTask.getWaitingTime();
        if (schedulerTask.getDispatchedTime() != null) this.dispatchedTime = schedulerTask.getDispatchedTime();
        if (schedulerTask.getStartTime() != null) this.startTime = schedulerTask.getStartTime();
        if (schedulerTask.getEndTime() != null) this.endTime = schedulerTask.getEndTime();
        if (schedulerTask.getCreateTime() != null) this.createTime = schedulerTask.getCreateTime();
        this.updateTime = now();

        return this;
    }

    public List<String> getListOfWorkerGroups() {
        if (this.getWorkerGroups() == null)
            return null;

        return on(",").splitToList(this.getWorkerGroups());
    }
}
