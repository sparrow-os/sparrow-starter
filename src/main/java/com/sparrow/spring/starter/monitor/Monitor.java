package com.sparrow.spring.starter.monitor;

import com.sparrow.constant.DateTime;
import com.sparrow.enums.Order;
import com.sparrow.support.IpSupport;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.DateTimeUtility;
import org.roaringbitmap.longlong.Roaring64Bitmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Monitor {
    private static Logger logger = LoggerFactory.getLogger(Monitor.class);
    private IpSupport ipSupport;

    public Monitor(IpSupport ipSupport) {
        this.ipSupport = ipSupport;
    }

    private final static int IP_COUNT_LIMIT = 1000;
    private final static int QPS_SECONDS_LIMIT = 60 * 60;

    /**
     * 按请求顺序排序
     * key：ip
     * value:请求数
     */
    private final LinkedHashMap<String, AtomicLong> accessCountPerIp = new LinkedHashMap<>();
    /**
     * 最近qps
     * key:秒级时间戮
     * value：请求数
     */
    private final LinkedHashMap<Long, AtomicLong> qps = new LinkedHashMap<>();

    /**
     * 每天的请求数，保证近一周
     * key:每天日期的时间戮
     * value:当天的请求量
     */
    private final LinkedHashMap<Long, AtomicLong> accessPerDay = new LinkedHashMap<>();

    /**
     * 总的IP数
     * Set<String> ipSet
     * Array long []
     * bit 图 hyper log log
     */
    private final Roaring64Bitmap ipCount = new Roaring64Bitmap();

    /**
     * 总的访问数
     */
    private final AtomicLong accessCount = new AtomicLong(0);

    public LinkedHashMap<String, Long> getAccessCountPerIp() {
        List<Map.Entry<String, AtomicLong>> ipAccessList = CollectionsUtility.sortMapByValue(this.accessCountPerIp, Order.DESC, (o1, o2) -> o1.get() > o2.get() ? 1 : -1);
        LinkedHashMap<String, Long> topIpAccessCountMap = new LinkedHashMap<>();
        for (Map.Entry<String, AtomicLong> entry : ipAccessList) {
            topIpAccessCountMap.put(entry.getKey(), entry.getValue().get());
            if (topIpAccessCountMap.size() > 100) {
                break;
            }
        }
        return topIpAccessCountMap;
    }

    public LinkedHashMap<String, Long> getQps() {
        int seconds = 5 * 60;
        //显示最近10分钟 600条
        LinkedHashMap<String, Long> topQps = new LinkedHashMap<>(seconds);
        for (Long key : this.qps.keySet()) {
            String secondKey = DateTimeUtility.getFormatTime(key*1000, "HH:mm:ss");
            topQps.put(secondKey, this.qps.get(key).get());
            if (topQps.size() > seconds) {
                break;
            }
        }
        return topQps;
    }

    public LinkedHashMap<String, Long> getAccessPerDay() {
        LinkedHashMap<String, Long> accessPerDay = new LinkedHashMap<>();
        for (Long dayOfMills : this.accessPerDay.keySet()) {
            String day = DateTimeUtility.getFormatTime(dayOfMills, DateTime.FORMAT_YYYY_MM_DD);
            accessPerDay.put(day, this.accessPerDay.get(dayOfMills).get());
        }
        return accessPerDay;
    }

    public Long getIpCount() {
        return ipCount.getLongCardinality();
    }

    public Long getAccessCount() {
        return accessCount.get();
    }

    private void increaseIpCount(Long numberIp) {
        this.ipCount.add(numberIp);
        this.accessCount.incrementAndGet();
    }

    private void incrementAccessCountByIp(String ip) {
        synchronized (this.accessCountPerIp) {
            CollectionsUtility.incrementByKey(this.accessCountPerIp, ip, IP_COUNT_LIMIT);
        }
    }


    public void incrementQps() {
        Long second = System.currentTimeMillis() / 1000;
        synchronized (this.qps) {
            CollectionsUtility.incrementByKey(this.qps, second, QPS_SECONDS_LIMIT);
        }
    }

    private void incrementAccessByDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        synchronized (this.accessPerDay) {
            CollectionsUtility.incrementByKey(this.accessPerDay, calendar.getTimeInMillis(), 30);
        }
    }

    public void access(String ip) {
        try {
            Long numberIp = this.ipSupport.toLong(ip);
            this.increaseIpCount(numberIp);
            this.incrementQps();
            this.incrementAccessByDay();
            this.incrementAccessCountByIp(ip);
        } catch (Exception e) {
            logger.error("monitor access error {}", ip, e);
        }
    }

    public MonitorResult result() {
        MonitorResult monitorResult = new MonitorResult();
        monitorResult.setAccessCount(this.accessCount.get());
        monitorResult.setIpCount(this.ipCount.getLongCardinality());
        monitorResult.setAccessCountPerIp(this.getAccessCountPerIp());
        monitorResult.setQps(this.getQps());
        monitorResult.setAccessCountPerDay(this.getAccessPerDay());
        return monitorResult;
    }
}
