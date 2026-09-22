/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.spring.starter.autoconfiguration.orm.release;

import com.sparrow.datasource.ConnectionPool;
import com.sparrow.datasource.ConnectionProxyContainer;
import com.sparrow.datasource.ConnectionReleaser;
import com.sparrow.datasource.SparrowConnectionReleaser;
import com.sparrow.spring.starter.autoconfiguration.DruidDataSourceAutoConfiguration;
import com.sparrow.spring.starter.autoconfiguration.SparrowDataSourceAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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

@ConditionalOnClass({SparrowConnectionReleaser.class, ConnectionProxyContainer.class, ConnectionPool.class})
@AutoConfigureAfter({SparrowDataSourceAutoConfiguration.class, DruidDataSourceAutoConfiguration.class})
@Slf4j
public class SparrowReleaserAutoConfiguration {
    public SparrowReleaserAutoConfiguration() {
        log.info("SparrowConnectionReleaserBuilder");
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionReleaser.class)
    @ConditionalOnBean({ConnectionProxyContainer.class, ConnectionPool.class})
    public SparrowConnectionReleaser connectionReleaser(ConnectionProxyContainer connectionProxyContainer) {
        return new SparrowConnectionReleaser(connectionProxyContainer);
    }
}
