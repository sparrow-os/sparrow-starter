package com.sparrow.spring.starter.filter;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public class SparrowCorsFilter extends CorsFilter{

    public SparrowCorsFilter(CorsConfigurationSource configSource) {
        super(configSource);
    }
}
