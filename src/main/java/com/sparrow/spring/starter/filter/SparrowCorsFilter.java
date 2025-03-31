package com.sparrow.spring.starter.filter;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public class SparrowCorsFilter extends CorsFilter implements OrderedFilter {
    private int order;
    public SparrowCorsFilter(CorsConfigurationSource configSource,int order) {
        super(configSource);
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
