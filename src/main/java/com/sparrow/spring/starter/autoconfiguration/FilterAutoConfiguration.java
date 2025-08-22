package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.spring.starter.SpringServletContainer;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.filter.*;
import com.sparrow.spring.starter.monitor.Monitor;
import com.sparrow.support.web.AbstractGlobalAttributeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.inject.Inject;
import java.util.List;

@Slf4j
public class FilterAutoConfiguration {
    public FilterAutoConfiguration() {
        log.info("Sparrow Auto Configuration INIT");
    }

    @Inject
    private SparrowConfig sparrowConfig;

    @Inject
    private SpringServletContainer springServletContainer;

    @Inject
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

    // 不要轻意加该配置会将SimpleUrlHandlerMapping 提前，导致应用程序无法自定义跳转
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/index");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

}
