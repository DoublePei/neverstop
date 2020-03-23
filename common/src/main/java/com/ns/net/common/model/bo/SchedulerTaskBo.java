package com.ns.net.common.model.bo;


import com.ns.net.common.grpc.RpcTask;
import com.ns.net.common.model.enums.JobPriority;
import com.ns.net.common.model.enums.JobType;
import com.ns.net.common.model.enums.TaskState;
import com.ns.net.common.model.enums.TaskTriggerType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.ComparisonChain.start;
import static com.google.common.collect.Ordering.natural;
import static com.ns.net.common.util.CronUtils.preScheduleTimeOfSomeTime;
import static com.ns.net.common.util.CronUtils.previousScheduleTimeOf;
import static com.ns.net.common.util.Dates.protoTimestamp;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.empty;
@Data
@Accessors(chain = true)
public class SchedulerTaskBo implements Comparable<SchedulerTaskBo> {
    private Long id;
    private Long jobId;
    private String taskName;
    private TaskState taskState;
    private String ossPath;
    private List<String> workerGroups;
    private String workerHost;
    private Integer workerPort;
    private String creatorName;
    private String creatorEmail;
    private Integer maxRetryTimes;
    private Long retryInterval;
    private Integer retryTimes;
    private Long executionTimeout;
    private Long pid;
    private Long dagId;
    private List<String> applicationId;
    private TaskTriggerType taskTriggerType;
    private JobPriority jobPriority;
    private JobType jobType;
    private Boolean isSelfDependent;
    private String scheduleCron;
    private Long offsetMs;
    private String sourceHost;
    private LocalDateTime scheduleTime; //quartz fire time, includes missing fire time
    private LocalDateTime pendingTime; //time of submit to task scheduler
    private LocalDateTime waitingTime; //time of submit to task dispatcher
    private LocalDateTime dispatchedTime; //time of dispatched to worker
    private LocalDateTime startTime; //time of start running
    private LocalDateTime endTime;  //time of success/fail/killed
    private Long elapseTime;
    private Integer parallelism;
    private LocalDateTime calculationTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer penalty = 0;
    private Optional<Boolean> isFirstOfJob = empty();
    private Set<Long> skipDependencies = new HashSet<>();
    private String taskDependenciesJson;

    public static SchedulerTaskBo from(SchedulerJobBo schedulerJobBo) {
        return new SchedulerTaskBo().setJobId(schedulerJobBo.getId())
                .setTaskName(schedulerJobBo.getJobName())
                .setOssPath(schedulerJobBo.getOssPath())
                .setWorkerGroups(schedulerJobBo.getWorkerGroups())
                .setMaxRetryTimes(schedulerJobBo.getMaxRetryTimes())
                .setRetryInterval(schedulerJobBo.getRetryInterval())
                .setExecutionTimeout(schedulerJobBo.getExecutionTimeout())
                .setScheduleTime(preScheduleTimeOfSomeTime(schedulerJobBo.getScheduleCron(), now()))
                .setJobPriority(schedulerJobBo.getJobPriority() != null ? schedulerJobBo.getJobPriority() : JobPriority.MEDIUM)
                .setJobType(schedulerJobBo.getJobType())
                .setIsSelfDependent(schedulerJobBo.getIsSelfDependent())
                .setScheduleCron(schedulerJobBo.getScheduleCron())
                .setOffsetMs(schedulerJobBo.getOffsetMs());
    }

    public RpcTask toRpcTask() {
        RpcTask.Builder builder = RpcTask.newBuilder();
        if (this.workerHost != null) builder.setWorkerHost(this.workerHost);
        if (this.executionTimeout != null) builder.setExecutionTimeout(this.executionTimeout);
        if (this.id != null) builder.setId(this.id);
        if (this.id != null) builder.setJobId(this.jobId);
        if (this.retryInterval != null) builder.setRetryInterval(this.retryInterval);
        if (this.scheduleTime != null) builder.setScheduleTime(protoTimestamp(scheduleTime));
        if (this.calculationTime != null) {
            builder.setCalculationTime(protoTimestamp(this.calculationTime));
        } else if (this.scheduleCron != null && this.scheduleTime != null) {
            builder.setCalculationTime(protoTimestamp(computeCalDate()));
        } else if (this.scheduleCron == null) { //job hasn't been scheduled but run manually
            builder.setCalculationTime(protoTimestamp(now()));
        }
        if (this.startTime != null) builder.setStartTime(protoTimestamp(this.startTime));
        if (this.endTime != null) builder.setEndTime(protoTimestamp(this.endTime));
        if (this.retryTimes != null) builder.setRetryTimes(retryTimes);
        if (this.taskState != null) builder.setState(taskState.getCode());
        if (this.workerPort != null) builder.setWorkerPort(workerPort);
        if (this.jobType != null) builder.setJobType(jobType.getCode());
        if (this.ossPath != null) builder.setOssPath(ossPath);
        return builder.build();
    }

    @Override
    public int compareTo(SchedulerTaskBo that) {
        return start().compare(this.jobPriority, that.jobPriority, natural().reverse())
                .compare(this.penalty, that.penalty)
                .compare(this.scheduleTime, that.scheduleTime, natural().nullsFirst())
                .result();
    }
    private LocalDateTime computeCalDate() {
        if (this.offsetMs == null || this.offsetMs == 0) {
            return previousScheduleTimeOf(this.scheduleCron, this.scheduleTime);
        }

        return this.scheduleTime.plus(this.offsetMs, MILLIS);
    }
}