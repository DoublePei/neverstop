package com.ns.net.common.model.enums;

public enum MetricAlert {
    KAFKA, JDBC;
    
    public static MetricAlert getAlertMetric(String name) {
        for(MetricAlert alertMetric : MetricAlert.values()) {
           if(alertMetric.toString().equalsIgnoreCase(name)) {
               return alertMetric;
           }
        }
        return KAFKA;
    }
}