package com.ns.net.manager;

import com.ns.net.common.exception.TaskNotFoundException;
import com.ns.net.common.model.bo.SchedulerTaskBo;
import com.ns.net.common.model.mapper.SchedulerTask;
import com.ns.net.common.repository.SchedulerTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ns.net.common.model.bo.SchedulerTaskBo.from;
import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class SchedulerDao {

    @Autowired
    private SchedulerTaskRepository schedulerTaskRepository;

    public SchedulerTaskBo saveTask(SchedulerTaskBo schedulerTaskBo) {
        SchedulerTask schedulerTask = schedulerTaskBo.toSchedulerTask();
        schedulerTask.setUpdateTime(now());
        final Long taskId = schedulerTask.getId();
        if (taskId == null) {
            schedulerTask.setCreateTime(schedulerTask.getUpdateTime());
            return from(schedulerTaskRepository.save(schedulerTask));
        }

        SchedulerTask schedulerTaskFromDb = schedulerTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        return from(schedulerTaskRepository.save(schedulerTaskFromDb.updateIgnoreNull(schedulerTask)));
    }
}
