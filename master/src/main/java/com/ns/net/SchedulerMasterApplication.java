package com.ns.net;

import com.ns.net.grpc.MasterGrpcService;
import com.ns.net.manager.QuartzScheduler;
import com.ns.net.manager.TaskDispatch;
import com.ns.net.manager.TaskScheduler;
import com.ns.net.manager.ZKElection;
import com.ns.net.manager.strategy.LeaderElectable;
import com.ns.net.common.model.interfaces.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.reverse;
import static java.lang.Runtime.getRuntime;
import static java.util.Arrays.asList;

/**
 * @author jiangpeipei
 */
@Slf4j
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class SchedulerMasterApplication implements LeaderElectable {

    @Autowired
    private ZKElection zkElection;

    @Autowired
    private QuartzScheduler quartzScheduler;

    @Autowired
    private TaskDispatch taskDispatch;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private MasterGrpcService masterGrpcService;

    private List<Scheduler> services = new ArrayList<>();
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(SchedulerMasterApplication.class, args);
    }

    /**
     * 逐个启动服务
     */
    @PostConstruct
    public void start() {
        zkElection.register(this);
        asList(zkElection
        ,quartzScheduler
        ,taskScheduler
        ,taskDispatch
        ,masterGrpcService).forEach(service -> {
            services.add(service);
            try {
                service.start();
            } catch (Exception e) {
                log.error("failed to start web ", e);
                stop();
            }
        });
        getRuntime().addShutdownHook(new Thread(() -> stop()));
    }

    @Override
    public void electedLeader() {
        log.info("Elected as a master");
    }

    @Override
    public void revokedLeadership() {
        stop();
    }

    private void stop() {
        reverse(services).forEach(service -> {
            try {
                service.stop();
            } catch (Exception e) {
                log.error("Master can not stop service", e);
            }
        });
        if (ctx != null && ctx.isActive()) {
            ctx.stop();
        }
    }
}
