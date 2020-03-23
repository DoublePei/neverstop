package com.ns.net.worker.grpc;

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
public class WorkerGrpcService implements Scheduler {

    @Value("${worker.server.port:8089}")
    private int workerPort;


    @Autowired
    private WorkerGrpc workerGrpc;
    private Server server;

    @Override
    public void start() {
        try {
            server = ServerBuilder.forPort(workerPort)
                    .addService(workerGrpc)
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

