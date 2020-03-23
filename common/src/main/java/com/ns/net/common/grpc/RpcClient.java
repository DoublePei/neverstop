package com.ns.net.common.grpc;

import com.ns.net.common.model.bo.SchedulerNodeBo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

@Slf4j
public class RpcClient implements AutoCloseable {


    private ManagedChannel channel;
    private SchedulerServiceGrpc.SchedulerServiceBlockingStub stub;

    private RpcClient(String host, Integer port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = SchedulerServiceGrpc.newBlockingStub(channel);
    }


    public static RpcClient create(String host, Integer port) {
        return new RpcClient(host, port);
    }

    public static RpcClient create(SchedulerNodeBo worker) {
        requireNonNull(worker, "Worker is null");
        requireNonNull(worker.getHost(), "Worker host is null");
        requireNonNull(worker.getPort(), "Worker port is null");

        return new RpcClient(worker.getHost(), worker.getPort());
    }

    public RpcResponse submitTask(RpcTask task) {
        RpcResponse response = stub.submitTask(task);
        if (response.getCode() == 500) {
            log.error("submit task failed");
        }
        return response;
    }

    public RpcResponse killTask(RpcTask task) {
        return stub.killTask(task);
    }

    public RpcResponse updateTask(RpcTask task) {
        return stub.updateTask(task);
    }

    public RpcResponse heartbeat(RpcHeartbeat heartbeat) {
        return stub.heartbeat(heartbeat);
    }

    public HealthCheckResponse healthCheck(HealthCheckRequest request) {
        return stub.healthCheck(request);
    }


    @Override
    public void close() throws Exception {
        shutdown();
    }

    public void shutdown() {
        try {
            if (channel.isShutdown())
                return;

            channel.shutdown()
                    .awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
    }
}
