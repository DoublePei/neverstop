package com.ns.net.manager.strategy;

/**
 * 可被zookeeper选举
 */
public interface LeaderElectable {

    void electedLeader();

    void revokedLeadership();
}
