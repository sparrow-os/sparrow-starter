package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.spring.starter.filter.ClientInformationFilter;
import com.sparrow.spring.starter.filter.SparrowCorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

public class FilterRegistrationBeanAutoConfiguration {

    private static final int CLIENT_INFORMATION_FILTER_ORDER = 0;
    private static final int CORS_FILTER_ORDER = 1;


    @Bean
    public FilterRegistrationBean<Filter> clientInformationFilterBean(ClientInformationFilter clientInformationFilter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(clientInformationFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("clientInformationFilter");
        filterRegistrationBean.setOrder(CLIENT_INFORMATION_FILTER_ORDER);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean<Filter> corsFilter(SparrowCorsFilter corsFilter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(corsFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("corsFilter");
        filterRegistrationBean.setOrder(CORS_FILTER_ORDER);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }
}
