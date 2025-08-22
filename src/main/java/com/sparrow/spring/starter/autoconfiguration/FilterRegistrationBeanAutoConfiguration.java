package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.spring.starter.filter.ClientInformationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

public class FilterRegistrationBeanAutoConfiguration {

    private static final int CLIENT_INFORMATION_FILTER_ORDER = 0;
    @Autowired
    private ClientInformationFilter clientInformationFilter;

    @Bean
    public FilterRegistrationBean<Filter> clientInformationFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(clientInformationFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("clientInformationFilter");
        filterRegistrationBean.setOrder(CLIENT_INFORMATION_FILTER_ORDER);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }
}
