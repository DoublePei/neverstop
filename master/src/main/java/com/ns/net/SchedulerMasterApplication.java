package com.ns.net;

import com.ns.net.manager.ZKElection;
import com.ns.net.manager.strategy.LeaderElectable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Runtime.getRuntime;
import static java.util.Arrays.asList;
import static com.google.common.collect.Lists.reverse;

/**
 * @author jiangpeipei
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.ns.net"})
public class SchedulerMasterApplication implements LeaderElectable {

    @Autowired
    private ZKElection zkElection;

    private List<Lifecycle> services = new ArrayList<>();
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
        asList(zkElection).forEach(service -> {
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
