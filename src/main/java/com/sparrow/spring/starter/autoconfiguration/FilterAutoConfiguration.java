package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.spring.starter.SpringServletContainer;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.filter.*;
import com.sparrow.spring.starter.monitor.Monitor;
import com.sparrow.spring.starter.resolver.ClientInfoArgumentResolvers;
import com.sparrow.support.web.AbstractGlobalAttributeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.List;

@Slf4j
public class FilterAutoConfiguration {
    public FilterAutoConfiguration() {
        log.info("Sparrow Auto Configuration INIT");
    }

    @Autowired
    private SparrowConfig sparrowConfig;

    @Autowired
    private SpringServletContainer springServletContainer;

    @Autowired
    private Monitor monitor;
    @Bean
    public FlashFilter flashFilter() {
        return new FlashFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "sparrow.cors", name = "allow", havingValue = "true")
    public SparrowCorsFilter sparrowCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        List<String> allowedOrigins = this.sparrowConfig.getCors().getAllowedOrigins();
        for (String allowedOrigin : allowedOrigins) {
            log.info("allowedOrigin: " + allowedOrigin);
            config.addAllowedOrigin(allowedOrigin);
        }
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new SparrowCorsFilter(source);
    }

    @Bean
    public ClientInformationFilter clientInformationFilter() {
        return new ClientInformationFilter(springServletContainer);
    }

    @Bean
    public AccessMonitorFilter accessMonitorFilter() {
        return new AccessMonitorFilter(monitor);
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
}
