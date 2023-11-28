package com.sparrow.spring.starter.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class OperateLimiter {

    public OperateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private StringRedisTemplate redisTemplate;

    public boolean renewalLimit(String key, Integer times, Long expires) {
        Long currentTimes = redisTemplate.opsForValue().increment(key);
        if (currentTimes == null) {
            return false;
        }
        if (currentTimes > times) {
            return false;
        }
        redisTemplate.expire(key, expires, TimeUnit.MILLISECONDS);
        return true;
    }

    public boolean limit(String key, Integer times, Long expires) {
        Long currentTimes = redisTemplate.opsForValue().increment(key);
        //null when used in pipeline / transaction.
        if (currentTimes == null) {
            return false;
        }
        if (currentTimes > times) {
            return false;
        }
        if (currentTimes == 1L) {
            redisTemplate.expire(key, expires, TimeUnit.MILLISECONDS);
        }
        return true;
    }
}
