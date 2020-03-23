package com.ns.net.worker.grpc;

import com.ns.net.common.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkerGrpc extends SchedulerServiceGrpc.SchedulerServiceImplBase {

    @Override
    public void submitTask(RpcTask rpcTask, StreamObserver<RpcResponse> responseObserver) {
        log.info("Get submit task request, rpcTask [{}]", rpcTask);
        RpcResponse.Builder builder = RpcResponse.newBuilder();
        builder.setCode(200);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void killTask(RpcTask rpcTask, StreamObserver<RpcResponse> responseObserver) {
        log.info("Get kill task request, rpcTask [{}]", rpcTask);
        RpcResponse.Builder builder = RpcResponse.newBuilder();
        builder.setCode(200);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void healthCheck(HealthCheckRequest rpcTask, StreamObserver<HealthCheckResponse> responseObserver) {
        log.info("Get health check request, rpcTask [{}]", rpcTask);
        HealthCheckResponse.Builder builder = HealthCheckResponse.newBuilder().setStatus(HealthCheckResponse.ServingStatus.SERVING);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}