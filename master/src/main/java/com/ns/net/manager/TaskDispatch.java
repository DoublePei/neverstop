package com.ns.net.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ns.net.common.model.bo.SchedulerTaskBo;
import com.ns.net.scheduler.SchedulerDispatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 检测worker的资源，如果有空闲资源则进行分发
 * 分发到rpcClient提交给worker执行
 */
@Slf4j
@Component
public class TaskDispatch implements SchedulerDispatch {

    private ThreadPoolExecutor threadPool;
    //优先级低的任务去运行
    private ThreadPoolExecutor limitedThreadPool;

    @Value("${services.task-dispatcher.thread.count:2}")
    private int threadCount;

    @Override
    public SchedulerTaskBo submit(SchedulerTaskBo task) {
        log.info("send dispatch to worker ");
        threadPool.execute(new TaskDispatchThread());
        return task;
    }

    @Override
    public boolean kill(SchedulerTaskBo task) {
        return false;
    }

    @Override
    public void start() throws Exception {
        this.threadPool = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactoryBuilder().setNameFormat("Task-dispatcher-cron").build(),
                new ThreadPoolExecutor.DiscardPolicy());
        this.limitedThreadPool = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactoryBuilder().setNameFormat("Task-dispatcher-limited").build(),
                new ThreadPoolExecutor.DiscardPolicy());
        //加载状态时waiting的任务
    }

    @Override
    public void stop() throws Exception {
        if (this.threadPool == null || this.threadPool.isShutdown())
            return;

        this.threadPool.shutdownNow();
    }

    private class TaskDispatchThread implements Runnable {

        @Override
        public void run() {
            log.info("哈哈哈哈");
        }
    }

}
