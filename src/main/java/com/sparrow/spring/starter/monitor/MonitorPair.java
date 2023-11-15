package com.sparrow.spring.starter.monitor;

public class MonitorPair {
    public MonitorPair(String key, Long value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private Long value;

    public String getKey() {
        return key;
    }

    public Long getValue() {
        return value;
    }
}
