package com.sparrow.spring.starter.autoconfiguration.orm.release;

import com.sparrow.datasource.ConnectionReleaser;
import com.sparrow.datasource.DefaultConnectionReleaser;
import com.sparrow.spring.starter.autoconfiguration.DruidDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OtherReleaserAutoConfiguration 之后装配
 * 如果ConnectionReleaser 实例存在，则说明装配成功
 */
@ConditionalOnClass(DefaultConnectionReleaser.class)
@AutoConfigureAfter({SparrowReleaserAutoConfiguration.class, DruidDataSourceAutoConfiguration.class})
public class OtherReleaserAutoConfiguration {
    public OtherReleaserAutoConfiguration() {
        System.out.println("DefaultConnectionReleaserBuilder");
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionReleaser.class)
    public DefaultConnectionReleaser connectionReleaser() {
        return new DefaultConnectionReleaser();
    }
}
