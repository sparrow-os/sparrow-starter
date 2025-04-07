package com.sparrow.spring.starter;

import org.springframework.context.annotation.Bean;

public class SpringContextAutoConfiguration {
    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }
}
