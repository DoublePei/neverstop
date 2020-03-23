package com.ns.net.grpc;

import com.ns.net.common.grpc.RpcHeartbeat;
import com.ns.net.common.grpc.RpcResponse;
import com.ns.net.common.grpc.RpcTask;
import com.ns.net.common.grpc.SchedulerServiceGrpc.SchedulerServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MasterGrpc extends SchedulerServiceImplBase {

    @Override
    public void heartbeat(RpcHeartbeat heartbeat, StreamObserver<RpcResponse> responseStreamObserver) {
        RpcResponse.Builder response = RpcResponse.newBuilder().setCode(200);

        log.debug("Receive worker {} heartbeat.", heartbeat.getHost());
        //更新worker
        responseStreamObserver.onNext(response.build());
        responseStreamObserver.onCompleted();
    }

    @Override
    public void updateTask(RpcTask rpcTask, StreamObserver<RpcResponse> responseStreamObserver) {
        RpcResponse.Builder response = RpcResponse.newBuilder().setCode(200);
        log.debug("Receive worker {} updateMessage.", rpcTask.getWorkerHost());
        responseStreamObserver.onNext(response.build());
        responseStreamObserver.onCompleted();
    }
}
