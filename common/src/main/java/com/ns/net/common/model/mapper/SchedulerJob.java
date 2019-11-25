package com.ns.net.common.model.mapper;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Splitter.on;

@Accessors(chain = true)
@Data
@Table("t_kepler_scheduler_job")
public class SchedulerJob {

    @Id
    private Long id;
    private String jobName;
    private Integer jobType;
    private Integer jobPriority;
    private String creatorId;
    private String creatorName;
    private String ownerIds;
    private String ownerNames;
    private String alertUsers;
    /**
     * 不同类型以逗号分割
     */
    private String alertIds;
    private String scheduleCron;
    private Boolean isSelfDependent;
    private Integer maxRetryTimes;
    private Long retryInterval;
    private Long executionTimeout;
    private String workerGroups;
    private Integer jobReleaseState;
    private String description;
    private String jobConfiguration;
    private Long businessLine;
    private String version;
    private String ossPath;
    private LocalDateTime startTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public List<String> getListOfWorkerGroups() {
        if (this.workerGroups == null)
            return null;

        return on(",").splitToList(this.getWorkerGroups());
    }
}