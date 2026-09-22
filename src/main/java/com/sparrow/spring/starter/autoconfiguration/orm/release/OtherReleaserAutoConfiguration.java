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

import com.sparrow.datasource.ConnectionReleaser;
import com.sparrow.datasource.DefaultConnectionReleaser;
import com.sparrow.spring.starter.autoconfiguration.DruidDataSourceAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * OtherReleaserAutoConfiguration 之后装配
 * 如果ConnectionReleaser 实例存在，则说明装配成功
 */
@ConditionalOnClass(DefaultConnectionReleaser.class)
@AutoConfigureAfter({SparrowReleaserAutoConfiguration.class, DruidDataSourceAutoConfiguration.class})
@Slf4j
public class OtherReleaserAutoConfiguration {
    public OtherReleaserAutoConfiguration() {
        log.info("DefaultConnectionReleaserBuilder");
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionReleaser.class)
    public DefaultConnectionReleaser connectionReleaser() {
        return new DefaultConnectionReleaser();
    }
}
