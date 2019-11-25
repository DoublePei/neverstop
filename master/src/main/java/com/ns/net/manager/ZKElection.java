package com.ns.net.manager;

import com.ns.net.common.model.ZkMaster;
import com.ns.net.common.util.CuratorUtils;
import com.ns.net.common.util.MetricsUtils;
import com.ns.net.manager.strategy.LeaderElectable;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.ns.net.common.constants.Constant.MASTER_GROUP;
import static org.apache.curator.framework.imps.CuratorFrameworkState.STOPPED;

@Slf4j
@Component
public class ZKElection implements LeaderLatchListener, Lifecycle, ConnectionStateListener {

    @Resource
    private CuratorFramework framework;
    private LeaderElectable master;
    private LeaderLatch leaderLatch;
    private LeadershipStatus status = LeadershipStatus.NOT_LEADER;

    public void register(LeaderElectable master) {
        this.master = master;
    }

    @Value("${server.port}")
    private int http_port;
    @Value("${rpc.port}")
    private int rpc_port = 9090;

    @Override
    public void start() {
        framework.getConnectionStateListenable()
                .addListener(this);
        try {
            leaderLatch = new LeaderLatch(framework, CuratorUtils.createPath(framework, MASTER_GROUP));
            leaderLatch.addListener(this);
            leaderLatch.start();
            leaderLatch.await();
        } catch (Exception e) {
            log.error("start zkServer failed...");
        }
    }

    /**
     * 选举为leader之后
     */
    @Override
    public void isLeader() {
        synchronized (this) {
            //判断当前实例是否是leader
            if (!leaderLatch.hasLeadership()) {
                return;
            }
            ZkMaster zkMaster = new ZkMaster(MetricsUtils.getHostIpAddress(), rpc_port, http_port);
            try {
                framework.setData().forPath(MASTER_GROUP, zkMaster.toString().getBytes("UTF-8"));
            } catch (Exception e) {
                log.error("set data error");
            }
            updateLeadershipStatus(true);
        }
    }

    private void updateLeadershipStatus(boolean isLeader) {
        if (isLeader && status == LeadershipStatus.NOT_LEADER) {
            status = LeadershipStatus.LEADER;
            master.electedLeader();
            //注册成功master
        } else if (!isLeader && status == LeadershipStatus.LEADER) {
            status = LeadershipStatus.NOT_LEADER;
            //取消注册master
            master.revokedLeadership();
        }
    }

    @Override
    public void notLeader() {

        if (leaderLatch.hasLeadership()) {
            return;
        }
        log.info("We have lost leadership");
        updateLeadershipStatus(false);
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        switch (newState) {
            case SUSPENDED:
            case RECONNECTED:
            case LOST:
            case READ_ONLY:
            case CONNECTED:
        }
    }

    @Override
    public void stop() {
        if (leaderLatch.getState() != LeaderLatch.State.CLOSED) {
            try {
                leaderLatch.close();
            } catch (IOException e) {
                log.error("close leaderLatch failed", e);
            }
        }
        if (framework.getState() != STOPPED) {
            framework.close();
        }
    }

    @Override
    public boolean isRunning() {
        return leaderLatch.hasLeadership();
    }

    private enum LeadershipStatus {
        LEADER, NOT_LEADER;
    }
}
