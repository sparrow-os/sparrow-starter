package com.sparrow.spring.test.monitor;

import com.sparrow.spring.starter.monitor.Monitor;

import java.util.Random;

public class MonitorTest {
    public static void main(String[] args) {
        Monitor monitor = new Monitor(null);
        for (int i = 0; i < 1000; i++) {
            monitor.incrementAccessCountByIp("192.168.1.1" + new Random().nextInt(100));
        }
        System.out.println(monitor.getAccessCountPerIp());
    }
}
