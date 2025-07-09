package com.sparrow.spring.starter;

import com.sparrow.spring.starter.config.SparrowConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(prefix = "sparrow.enums", name = {"coder","business"})
@Slf4j
public class EnumsAutoConfiguration {
    public EnumsAutoConfiguration() {
        log.info("EnumsAutoConfiguration init");
    }

    @Bean
    public EnumsContainer businessEnumsContainer(SparrowConfig sparrowConfig) throws ClassNotFoundException {
        return new EnumsContainer(sparrowConfig.getEnums().getBusiness());
    }

    @Bean
    public EnumsContainer coderEnumsContainer(SparrowConfig sparrowConfig) throws ClassNotFoundException {
        return new EnumsContainer(sparrowConfig.getEnums().getCoder());
    }
}
