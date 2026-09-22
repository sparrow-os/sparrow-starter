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
package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.servlet.CaptchaServlet;
import com.sparrow.servlet.impl.MockCaptchaService;
import com.sparrow.servlet.impl.SessionCaptchaService;
import com.sparrow.spring.container.SpringServletContainer;
import com.sparrow.spring.redis.RedisCaptchaService;
import com.sparrow.support.CaptchaService;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class CaptchaAutoConfiguration {

    public CaptchaAutoConfiguration() {
        log.info("CaptchaAutoConfiguration init");
    }


    @Autowired
    private SpringServletContainer springServletContainer;

    @Bean
    @ConditionalOnProperty(prefix = "sparrow", name = "captcha.service", havingValue = "mock")
    @ConditionalOnMissingBean(CaptchaService.class)
    public MockCaptchaService mockCaptchaService() {
        return new MockCaptchaService();
    }

    @Bean
    @ConditionalOnProperty(prefix = "sparrow", name = "captcha.service", havingValue = "session")
    @ConditionalOnMissingBean(CaptchaService.class)
    public SessionCaptchaService sessionCaptchaService() {
        return new SessionCaptchaService(this.springServletContainer);
    }


    @ConditionalOnProperty(prefix = "sparrow", name = "captcha.service", havingValue = "redis")
    @ConditionalOnClass(RedisTemplate.class)
    public static class RedisCaptchaServiceConfig {
        @Bean
        @ConditionalOnMissingBean(CaptchaService.class)
        public RedisCaptchaService redisCaptchaService(@Named("redisTemplate") RedisTemplate redisTemplate) {
            return new RedisCaptchaService(redisTemplate);
        }
    }


    @Bean
    @ConditionalOnBean(CaptchaService.class)
    public ServletRegistrationBean captcha(CaptchaService captchaService) {
        return new ServletRegistrationBean<>(new CaptchaServlet(captchaService), "/captcha");
    }
}
