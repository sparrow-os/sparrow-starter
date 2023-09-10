package com.sparrow.spring.test.monitor;

import com.sparrow.spring.starter.monitor.Monitor;

public class MonitorTest {
    public static void main(String[] args) {
        Monitor monitor = new Monitor(null);
        for (int i = 0; i < 100; i++) {
            monitor.incrementQps();
        }
        System.out.println(monitor.getQps());
    }
}
