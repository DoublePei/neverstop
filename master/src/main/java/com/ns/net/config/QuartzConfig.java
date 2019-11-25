package com.ns.net.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
@ConfigurationProperties(prefix="quartz")
public class QuartzConfig extends Properties {
}
