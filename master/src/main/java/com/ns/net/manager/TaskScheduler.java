package com.ns.net.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ns.net.common.model.bo.SchedulerTaskBo;
import com.ns.net.scheduler.SchedulerDispatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.ns.net.common.model.enums.TaskState.PENDING;
import static java.time.LocalDateTime.now;

/**
 * 主要是进行依赖的检测,如果依赖全部符合，则进行分发到下一层进行机器的检测
 */
@Slf4j
@Component
public class TaskScheduler implements SchedulerDispatch {

    @Value("${services.task-scheduler.thread.count:2}")
    private Integer threadCount;

    private ThreadPoolExecutor threadPool;
    @Autowired
    private SchedulerDao schedulerDao;
    @Autowired
    private JobStateStore jobStateStore;
    @Autowired
    private TaskDispatch taskDispatch;

    /**
     * 功能点
     *
     * @param task
     * @return
     */
    @Override
    public SchedulerTaskBo submit(SchedulerTaskBo task) {
        SchedulerTaskBo taskUpdated = schedulerDao.saveTask(task.setTaskState(PENDING)
                .setPendingTime(now()));
        taskUpdated.setSkipDependencies(task.getSkipDependencies());
        threadPool.execute(new TaskSchedulerThread(task));
//        jobStateStore.removeTaskSuccessRecord(taskUpdated);
        //从jobStateStore移除历史任务
        return task;
    }

    @Override
    public boolean kill(SchedulerTaskBo task) {
        return false;
    }

    @Override
    public void start() throws Exception {
        //注册到quartz job
        QuartzJob.init(this);
        //启动分发检测线程
        threadPool = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("Task-schedule-%d").build()
                , new ThreadPoolExecutor.DiscardOldestPolicy());
        //load数据库中状态为pending的数据
        //todo

    }

    @Override
    public void stop() throws Exception {
        if (this.threadPool == null || this.threadPool.isShutdown())
            return;

        this.threadPool.shutdownNow();
    }

    private class TaskSchedulerThread implements Runnable {

        private SchedulerTaskBo schedulerTaskBo;

        public TaskSchedulerThread(SchedulerTaskBo schedulerTaskBo) {
            this.schedulerTaskBo = schedulerTaskBo;
        }

        @Override
        public void run() {
            taskDispatch.submit(schedulerTaskBo);
            return;
        }
    }

}
