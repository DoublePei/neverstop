package com.ns.net.worker;


import com.ns.net.worker.grpc.WorkerGrpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class WorkerLauncher {

    @Autowired
    private WorkerGrpcService workerGrpcService;


    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(WorkerLauncher.class, args);
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws IOException, InterruptedException {
        //注册自己到zk
//        ZkUtils.regist(workerPort, zkClientHolder.getZkClient());
        //启动心跳线程
//        heartbeatSes.scheduleWithFixedDelay(heartbeatThread, 0L, heartbeatInterval, TimeUnit.MILLISECONDS);
        //检查master指令
//        handleMasterCmdSes.scheduleWithFixedDelay(handleMasterCommandThread, 0L, Constants.CHECK_MASTER_CMD_INTERVAL, TimeUnit.MILLISECONDS);

        //启动rpc服务
        registHook(ctx);
        workerGrpcService.start();
        }

    private void registHook(ConfigurableApplicationContext context) {
        Runtime.getRuntime().addShutdownHook(new ShutDownHookThread(context));
    }

    private class ShutDownHookThread extends Thread {

        private ConfigurableApplicationContext context;

        public ShutDownHookThread(ConfigurableApplicationContext context) {
            this.context = context;
        }

        @Override
        public void run() {

            log.info("shut down hook running.");

            log.info("stop heart beat.");
            //关闭心跳
//            if(null != heartbeatSes) {
//                heartbeatSes.shutdownNow();
//            }

//            //关闭master指令监控调度
//            if(null != handleMasterCmdSes) {
//                handleMasterCmdSes.shutdownNow();
//            }

            log.info("unregist self from zookeeper.");
            //卸载
//            ZkUtils.unRegist(workerPort, zkClientHolder.getZkClient());

            log.info("kill all running task.");
            //停止所有的运行任务
//            CommonUtils.killAllRunningTasks(zkClientHolder, rpcClientHolder, TaskState.FAILOVER);

            log.info("shut down grpc server.");
            if (workerGrpcService != null) {
                workerGrpcService.stop();
            }
            log.info("close spring context.");
            context.close();

            log.warn("shut down hook run success.");
        }
    }

}
