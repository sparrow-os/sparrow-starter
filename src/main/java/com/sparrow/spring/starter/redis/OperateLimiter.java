package com.sparrow.spring.starter.redis;

public interface OperateLimiter {
    boolean renewalLimit(String key, Integer times, Long expires);

    boolean limit(String key, Integer times, Long expires);
}
