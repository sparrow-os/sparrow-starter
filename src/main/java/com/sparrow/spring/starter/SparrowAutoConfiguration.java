package com.sparrow.spring.starter;

import com.sparrow.image.ImageExtractorRegistry;
import com.sparrow.io.FileService;
import com.sparrow.io.impl.JDKFileService;
import com.sparrow.spring.starter.Interceptor.FlashParamPrepareAspect;
import com.sparrow.spring.starter.redis.OperateLimiter;
import com.sparrow.spring.starter.redis.RedisOperateLimiter;
import com.sparrow.support.IpSupport;
import com.sparrow.support.ip.SparrowIpSupport;
import com.sparrow.support.web.CookieUtility;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class SparrowAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SpringContainer.class)
    public SpringContainer springContainer() {
        return new SpringContainer();
    }

    @Bean
    @ConditionalOnMissingBean(SpringContext.class)
    public SpringContext springContext() {
        return new SpringContext();
    }

    @Bean
    @ConditionalOnMissingBean(CookieUtility.class)
    public CookieUtility cookieUtility() {
        return new CookieUtility();
    }

    @Bean
    @ConditionalOnMissingBean(SpringServletContainer.class)
    public SpringServletContainer springServletContainer() {
        return new SpringServletContainer();
    }

    @Bean
    @ConditionalOnMissingBean(FlashParamPrepareAspect.class)
    public FlashParamPrepareAspect flashParamPrepareAspect() {
        return new FlashParamPrepareAspect();
    }

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService fileService() {
        return new JDKFileService();
    }

    @Bean
    @ConditionalOnMissingBean(IpSupport.class)
    public IpSupport ipSupport() {
        return new SparrowIpSupport();
    }

    @Bean
    @ConditionalOnMissingBean(ImageExtractorRegistry.class)
    public ImageExtractorRegistry imageExtractorRegistry() {
        return new ImageExtractorRegistry();
    }

}
