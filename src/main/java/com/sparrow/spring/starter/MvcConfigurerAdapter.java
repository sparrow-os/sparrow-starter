package com.sparrow.spring.starter;

import com.sparrow.servlet.CaptchaServlet;
import com.sparrow.servlet.impl.SessionCaptchaService;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.filter.ClientInformationFilter;
import com.sparrow.spring.starter.filter.FlashFilter;
import com.sparrow.spring.starter.filter.SparrowCorsFilter;
import com.sparrow.spring.starter.filter.SpringGlobalAttributeFilter;
import com.sparrow.spring.starter.resolver.ClientInfoArgumentResolvers;
import com.sparrow.spring.starter.resolver.LoginUserArgumentResolvers;
import com.sparrow.spring.starter.servlet.RedisCaptchaService;
import com.sparrow.support.CaptchaService;
import com.sparrow.support.web.AbstractGlobalAttributeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
@AutoConfigureAfter(SparrowConfig.class)
public class MvcConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    public MvcConfigurerAdapter() {
        logger.info("MvcConfigurerAdapter init");
    }

    @Autowired
    private SparrowConfig sparrowConfig;

    @Bean
    public FlashFilter flashFilter() {
        return new FlashFilter();
    }

    @Bean
    public SpringServletContainer springServletContainer() {
        return new SpringServletContainer();
    }

    @Bean
    @ConditionalOnProperty(prefix = "sparrow.cors", name = "allow", havingValue = "true")
    public SparrowCorsFilter sparrowCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        List<String> allowedOrigins = this.sparrowConfig.getCors().getAllowedOrigins();
        for (String allowedOrigin : allowedOrigins) {
            logger.info("allowedOrigin: " + allowedOrigin);
            config.addAllowedOrigin(allowedOrigin);
        }
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new SparrowCorsFilter(source, 0);
    }

    @Bean
    public ClientInformationFilter clientInformationFilter() {
        return new ClientInformationFilter(-98, springServletContainer());
    }

    @Bean
    public ClientInfoArgumentResolvers clientInfoArgumentResolvers() {
        return new ClientInfoArgumentResolvers();
    }

    @Bean
    public LoginUserArgumentResolvers loginTokenArgumentResolvers() {
        return new LoginUserArgumentResolvers();
    }

    @Bean
    public AbstractGlobalAttributeFilter globalAttributeFilter() {
        return new SpringGlobalAttributeFilter(this.sparrowConfig);
    }

    @Bean
    public FilterRegistrationBean<Filter> flashFilterBean() {
        FilterRegistrationBean<Filter> globalAttributeFilterBean = new FilterRegistrationBean<>();
        globalAttributeFilterBean.setFilter(flashFilter());
        globalAttributeFilterBean.addUrlPatterns("/*");
        globalAttributeFilterBean.setName("flashFilter");
        globalAttributeFilterBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return globalAttributeFilterBean;
    }

    // 不要轻意加该配置会将SimpleUrlHandlerMapping 提前，导致应用程序无法自定义跳转
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/index");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }


    @Bean
    public FilterRegistrationBean<Filter> globalAttributeFilterBean() {
        FilterRegistrationBean<Filter> globalAttributeFilterBean = new FilterRegistrationBean<>();
        globalAttributeFilterBean.setFilter(globalAttributeFilter());
        globalAttributeFilterBean.addUrlPatterns("/*");
        globalAttributeFilterBean.setName("globalAttributeFilter");
        globalAttributeFilterBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return globalAttributeFilterBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> clientInformationFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(clientInformationFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("clientInformationFilter");
        filterRegistrationBean.setOrder(0);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    /**
     * <pre>
     *     HttpMessageConvertersAutoConfiguration
     *     @Bean
     *     @ConditionalOnMissingBean
     *     public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
     *         return new HttpMessageConverters((Collection)converters.orderedStream().collect(Collectors.toList()));
     *     }
     * </pre>
     * 会自动配置 下文不需要
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /**
         * 只对输出结果对象提供转换
         * 基本数据类型直接返回Result,不支持封装转换
         */
//        converters.add(this.jsonMessageConverter);
//        converters.add(this.listJsonMessageConverter);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.clientInfoArgumentResolvers());
        argumentResolvers.add(this.loginTokenArgumentResolvers());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    /**
     * spring 跨域拦截器配置，在filter 中返回的情况无法生效
     * 如果在拦截器之前生效需要配置 CorsFilter
     *
     * @param registry
     * @see SparrowCorsFilter
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (this.sparrowConfig.getCors()==null||!this.sparrowConfig.getCors().isAllow()) {
            logger.warn("cors config not found !");
            return;
        }
        List<String> allowedOrigins = this.sparrowConfig.getCors().getAllowedOrigins();
        String[] allowedOriginArray = new String[allowedOrigins.size()];
        allowedOrigins.toArray(allowedOriginArray);
        registry.addMapping("/**")
                .allowedOrigins(allowedOriginArray)
                .allowCredentials(true)
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").maxAge(3600).allowCredentials(true);
    }

    @Bean
    @ConditionalOnProperty(prefix = "sparrow", name = "captcha.service", havingValue = "session")
    public SessionCaptchaService sessionCaptchaService() {
        return new SessionCaptchaService(springServletContainer());
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
