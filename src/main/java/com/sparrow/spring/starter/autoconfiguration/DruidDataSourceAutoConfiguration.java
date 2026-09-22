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
package com.sparrow.spring.starter.autoconfiguration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.sparrow.spring.config.SparrowConfig;
import com.sparrow.spring.datasource.druid.DruidCustomPasswordCallback;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore({SparrowDataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@AutoConfigureAfter(SparrowConfig.class)
@Slf4j
public class DruidDataSourceAutoConfiguration {

    public DruidDataSourceAutoConfiguration() {
        log.info("DruidDataSourceAutoConfiguration init");
    }
    @Autowired
    private SparrowConfig sparrowConfig;

    @Inject
    public DruidCustomPasswordCallback passwordCallback() {
        if (this.sparrowConfig.getDataSource() == null) {
            log.warn("data source config not found !");
            return null;
        }
        return new DruidCustomPasswordCallback(this.sparrowConfig.getDataSource().getPasswordKey(),
                this.sparrowConfig.getDataSource().getDebugDatasourcePassword());
    }

    @Bean("sparrow_default")
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnClass(DruidDataSource.class)
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    /**
     *  @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource")
     * druid-spring-boot-starter 依赖自动生效 druid，可以不配置 type 属性，但建议配置
     */
    public DataSource sparrow_default() {
        DruidDataSourceWrapper dataSource = new DruidDataSourceWrapper();
        dataSource.setPasswordCallback(passwordCallback());
        return dataSource;
    }
}
