package com.sparrow.spring.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringContextAutoConfiguration {
    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }
}
