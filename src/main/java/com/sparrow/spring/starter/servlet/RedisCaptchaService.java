package com.sparrow.spring.starter.servlet;

import com.sparrow.support.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisCaptchaService implements CaptchaService {
    private static final String REDIS_CAPTCHA_KEY = "captcha";
    private RedisTemplate redisTemplate;


    public RedisCaptchaService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getCaptcha(String sessionId) {
        String captcha= (String) this.redisTemplate.opsForValue().get(this.getRedisCaptchaKey(sessionId));
        log.debug("get captcha from redis, sessionId:{}, captcha:{}", sessionId, captcha);
        return captcha;
    }

    @Override
    public void setCaptcha(String sessionId, String captcha) {
        log.debug("set captcha to redis, sessionId:{}, captcha:{}", sessionId, captcha);
        this.redisTemplate.opsForValue().set(this.getRedisCaptchaKey(sessionId), captcha, 10, TimeUnit.MINUTES);
    }

    private String getRedisCaptchaKey(String sessionId) {
        return REDIS_CAPTCHA_KEY + ":" + sessionId;
    }
}
