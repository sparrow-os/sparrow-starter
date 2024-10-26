package com.sparrow.spring.starter.servlet;

import com.sparrow.support.CaptchaService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisCaptchaService implements CaptchaService {
    private static final String REDIS_CAPTCHA_KEY = "captcha.";
    private RedisTemplate redisTemplate;


    public RedisCaptchaService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getCaptcha(String sessionId) {
        return (String) this.redisTemplate.opsForValue().get(this.getRedisCaptchaKey(sessionId));
    }

    @Override
    public void setCaptcha(String sessionId, String captcha) {
        this.redisTemplate.opsForValue().set(this.getRedisCaptchaKey(sessionId), captcha, 1, TimeUnit.MINUTES);
    }

    private String getRedisCaptchaKey(String sessionId) {
        return REDIS_CAPTCHA_KEY + ":" + sessionId;
    }
}
