package com.sparrow.spring.starter.monitor;

import com.sparrow.protocol.POJO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MonitorResult implements POJO {
    private LinkedHashMap<String, Long> accessCountPerIp;

    /**
     * key:毫秒时间戮
     * value：请求数
     */
    private LinkedHashMap<String, Long> qps;

    /**
     * 每天的请求数，近一周
     */
    private LinkedHashMap<String, Long> accessCountPerDay;

    /**
     * 总的IP数
     */
    private Long ipCount;

    /**
     * 总的访问数
     */
    private Long accessCount;

    /**
     * 当前在线用户列表
     * key:用户id
     * value：channel info
     */
    private Map<String, String> onlineUserMap = new HashMap<>();

    public LinkedHashMap<String, Long> getAccessCountPerIp() {
        return accessCountPerIp;
    }

    public void setAccessCountPerIp(LinkedHashMap<String, Long> accessCountPerIp) {
        this.accessCountPerIp = accessCountPerIp;
    }

    public LinkedHashMap<String, Long> getQps() {
        return qps;
    }

    public void setQps(LinkedHashMap<String, Long> qps) {
        this.qps = qps;
    }

    public LinkedHashMap<String, Long> getAccessCountPerDay() {
        return accessCountPerDay;
    }

    public void setAccessCountPerDay(LinkedHashMap<String, Long> accessCountPerDay) {
        this.accessCountPerDay = accessCountPerDay;
    }

    public Long getIpCount() {
        return ipCount;
    }

    public void setIpCount(Long ipCount) {
        this.ipCount = ipCount;
    }

    public Long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Long accessCount) {
        this.accessCount = accessCount;
    }

    public Map<String, String> getOnlineUserMap() {
        return onlineUserMap;
    }

    public void setOnlineUserMap(Map<String, String> onlineUserMap) {
        this.onlineUserMap = onlineUserMap;
    }
}
