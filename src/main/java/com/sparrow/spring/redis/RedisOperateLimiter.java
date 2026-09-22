/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.spring.redis;

import jakarta.inject.Named;
import org.springframework.data.redis.core.RedisTemplate;

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
