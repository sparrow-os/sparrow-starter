package com.sparrow.spring.redis;

import jakarta.inject.Named;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisOperateLimiter implements OperateLimiter {

    public RedisOperateLimiter(@Named("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private RedisTemplate redisTemplate;

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
