package com.ns.net.grpc;

import com.ns.net.common.model.interfaces.Scheduler;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MasterGrpcService implements Scheduler {

    @Value("${rpc.port:9090}")
    private int DEFAULT_PORT = 9090;


    @Autowired
    private MasterGrpc masterGrpc;
    private Server server;

    @Override
    public void start() {
        try {
            server = ServerBuilder.forPort(DEFAULT_PORT)
                    .addService(masterGrpc)
                    .build()
                    .start();
        } catch (
                IOException e) {
            log.info("Rpc server start failed", e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            try {
                server.shutdown()
                        .awaitTermination(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("Rpc server stop failed", e);
            }
        }
    }
}
