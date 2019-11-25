package com.ns.net.common.model.mapper;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Table("t_kepler_scheduler_job_relation")
public class SchedulerJobRelation {
    @Id
    private Long id;
    private Long jobId;
    private Long parentJobId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static SchedulerJobRelation of(Long jobId, Long parentJobId) {
        return new SchedulerJobRelation().setJobId(jobId)
                .setParentJobId(parentJobId)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
    }
}
