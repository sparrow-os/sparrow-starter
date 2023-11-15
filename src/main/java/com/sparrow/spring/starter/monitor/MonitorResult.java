package com.sparrow.spring.starter.monitor;

import com.sparrow.protocol.POJO;

import java.util.List;
import java.util.Map;

public class MonitorResult implements POJO {
    /**
     * 用linked hash map 会有顺序问题
     */
    private List<MonitorPair> accessCountPerIp;

    /**
     * key:毫秒时间戮
     * value：请求数
     */
    private List<MonitorPair> qps;

    /**
     * 每天的请求数，近一周
     */
    private List<MonitorPair> accessCountPerDay;

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
    private Map<String, String> onlineUserMap;

    public List<MonitorPair> getAccessCountPerIp() {
        return accessCountPerIp;
    }

    public void setAccessCountPerIp(List<MonitorPair> accessCountPerIp) {
        this.accessCountPerIp = accessCountPerIp;
    }

    public List<MonitorPair> getQps() {
        return qps;
    }

    public void setQps(List<MonitorPair> qps) {
        this.qps = qps;
    }

    public List<MonitorPair> getAccessCountPerDay() {
        return accessCountPerDay;
    }

    public void setAccessCountPerDay(List<MonitorPair> accessCountPerDay) {
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
