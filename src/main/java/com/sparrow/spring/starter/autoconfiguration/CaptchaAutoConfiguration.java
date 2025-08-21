package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.servlet.CaptchaServlet;
import com.sparrow.servlet.impl.SessionCaptchaService;
import com.sparrow.spring.starter.SpringServletContainer;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.servlet.RedisCaptchaService;
import com.sparrow.support.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfigureAfter(SparrowConfig.class)
@Slf4j
public class CaptchaAutoConfiguration {

    public CaptchaAutoConfiguration() {
        log.info("CaptchaAutoConfiguration init");
    }


    @Autowired
    private SpringServletContainer springServletContainer;


    @Bean
    @ConditionalOnProperty(prefix = "sparrow", name = "captcha.service", havingValue = "session")
    public SessionCaptchaService sessionCaptchaService() {
        return new SessionCaptchaService(this.springServletContainer);
    }


    @ConditionalOnProperty(prefix = "sparrow", name = "captcha.service", havingValue = "redis")
    @ConditionalOnClass(RedisTemplate.class)
    public static class RedisCaptchaServiceConfig {
        @Bean
        public RedisCaptchaService redisCaptchaService(RedisTemplate redisTemplate) {
            return new RedisCaptchaService(redisTemplate);
        }
    }


    @Bean
    @ConditionalOnBean(CaptchaService.class)
    public ServletRegistrationBean captcha(CaptchaService captchaService) {
        return new ServletRegistrationBean<>(new CaptchaServlet(captchaService), "/captcha");
    }
}
