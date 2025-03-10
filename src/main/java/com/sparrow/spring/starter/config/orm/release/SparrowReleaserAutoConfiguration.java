package com.sparrow.spring.starter.config.orm.release;

import com.sparrow.datasource.ConnectionPool;
import com.sparrow.datasource.ConnectionProxyContainer;
import com.sparrow.datasource.ConnectionReleaser;
import com.sparrow.datasource.SparrowConnectionReleaser;
import com.sparrow.spring.starter.DruidDataSourceAutoConfiguration;
import com.sparrow.spring.starter.SparrowDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 如果依赖sparrow 自己数据源，则需要自定义释放器
 * 如果
 * SparrowConnectionReleaser.class
 * ConnectionProxyContainer.class
 * 类存在
 * <p>
 * 并且ConnectionProxyContainer 实例与
 * ConnectionPool 实例同时存在，则一定是sparrow 自己的数据源
 */
@Configuration
@ConditionalOnClass({SparrowConnectionReleaser.class, ConnectionProxyContainer.class, ConnectionPool.class})
@AutoConfigureAfter({SparrowDataSourceAutoConfiguration.class, DruidDataSourceAutoConfiguration.class})
public class SparrowReleaserAutoConfiguration {
    public SparrowReleaserAutoConfiguration() {
        System.out.println("SparrowConnectionReleaserBuilder");
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionReleaser.class)
    @ConditionalOnBean({ConnectionProxyContainer.class, ConnectionPool.class})
    public SparrowConnectionReleaser connectionReleaser(ConnectionProxyContainer connectionProxyContainer) {
        return new SparrowConnectionReleaser(connectionProxyContainer);
    }
}
