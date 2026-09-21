package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.spring.filter.*;
import com.sparrow.spring.container.SpringServletContainer;
import com.sparrow.spring.config.SparrowConfig;
import com.sparrow.spring.filter.monitor.Monitor;
import com.sparrow.support.web.AbstractGlobalAttributeFilter;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
public class FilterAutoConfiguration {
    public FilterAutoConfiguration() {
        log.info("FilterAutoConfiguration INIT");
    }

    @Inject
    private SparrowConfig sparrowConfig;

    @Inject
    private SpringServletContainer springServletContainer;

    @Inject
    private Monitor monitor;

    @Bean
    public ClientInformationFilter clientInformationFilter() {
        return new ClientInformationFilter(springServletContainer);
    }

    /**
     * FilterRegistrationBean 自定义过滤器行为
     * 设置优先级和 url patterns
     *
     * @param clientInformationFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> clientInformationFilterBean(ClientInformationFilter clientInformationFilter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(clientInformationFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("clientInformationFilter");
        filterRegistrationBean.setOrder(FilterOrders.CLIENT_INFORMATION_FILTER_ORDER);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Bean
    public FlashFilter flashFilter() {
        return new FlashFilter();
    }

    /**
     * 还有另一种配置方法spring mvc
     *
     * @return
     * @see MvcConfigurerAdapter addCorsMappings 拦截器机制
     * 该方法为filter 机制 时机比拦截器早
     */
    @Bean
    @ConditionalOnProperty(prefix = "sparrow.cors", name = "allow", havingValue = "true")
    public SparrowCorsFilter sparrowCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        List<String> allowedOrigins = this.sparrowConfig.getCors().getAllowedOrigins();
        for (String allowedOrigin : allowedOrigins) {
            log.info("allowedOrigin: {}", allowedOrigin);
        }
        config.setAllowedOriginPatterns(allowedOrigins);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new SparrowCorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean<Filter> corsFilter(SparrowCorsFilter corsFilter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(corsFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("corsFilter");
        filterRegistrationBean.setOrder(FilterOrders.CORS_FILTER_ORDER);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
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
