package com.sparrow.spring.starter;

import com.sparrow.spring.starter.config.SparrowConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(prefix = "sparrow", name = "enums")
public class EnumsAutoConfiguration {
    @Bean
    public EnumsContainer businessEnumsContainer(SparrowConfig sparrowConfig) throws ClassNotFoundException {
        return new EnumsContainer(sparrowConfig.getEnums().getBusiness());
    }

    @Bean
    public EnumsContainer coderEnumsContainer(SparrowConfig sparrowConfig) throws ClassNotFoundException {
        return new EnumsContainer(sparrowConfig.getEnums().getCoder());
    }
}
