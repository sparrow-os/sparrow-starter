package com.sparrow.spring.starter;

import com.sparrow.spring.starter.Interceptor.FlashParamPrepareAspect;
import com.sparrow.support.web.CookieUtility;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparrowAutoConfiguration {

    @Bean
    public SpringContainer springContainer() {
        return new SpringContainer();
    }

    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }

    @Bean
    public CookieUtility cookieUtility() {
        return new CookieUtility();
    }

    @Bean
    @ConditionalOnMissingBean(SpringServletContainer.class)
    public SpringServletContainer springServletContainer() {
        return new SpringServletContainer();
    }


    @Bean
    public FlashParamPrepareAspect flashParamPrepareAspect() {
        return new FlashParamPrepareAspect();
    }
}
