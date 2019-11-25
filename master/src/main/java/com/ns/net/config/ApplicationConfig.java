package com.ns.net.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.curator.framework.CuratorFrameworkFactory.newClient;

@Configuration
public class ApplicationConfig {

    @Value("${zookeeper.quorum}")
    private String zkUrl;
    @Value("${zookeeper.sessionTimeout:15000}")
    private int sessionTimeout;
    @Value("${zookeeper.connectionTimout:60000}")
    private int connectionTimeout;
    @Value("${zookeeper.retryWait:5000}")
    private int retryWait;
    @Value("${zookeeper.retryAttempts:3}")
    private int reconnectAttempts;

    @Bean
    public CuratorFramework framework() {
        final CuratorFramework client = newClient(
                zkUrl,
                sessionTimeout,
                connectionTimeout,
                new ExponentialBackoffRetry(retryWait, reconnectAttempts));
        client.start();
        return client;
    }
}
