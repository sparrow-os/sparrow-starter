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

import com.sparrow.datasource.ConnectionPool;
import com.sparrow.datasource.ConnectionProxyContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Slf4j
@ConditionalOnClass(ConnectionPool.class)
public class SparrowDataSourceAutoConfiguration {
    @Bean
    public ConnectionProxyContainer connectionProxyContainer() {
        return new ConnectionProxyContainer();
    }


    /**
     * <pre>
     *     private String[] getNames(Map<String, Object> annotationAttributes) {
     *             String[] value = (String[])((String[])annotationAttributes.get("value"));
     *             String[] name = (String[])((String[])annotationAttributes.get("name"));
     *             //name 或value必须有一个
     *             Assert.state(value.length > 0 || name.length > 0, "The name or value attribute of @ConditionalOnProperty must be specified");
     *             //name 和value 是互斥的
     *             @ConditionalOnProperty(prefix = "sparrow",value ="ds" ,name = "ds", havingValue = "sparrow")
     *             Assert.state(value.length == 0 || name.length == 0, "The name and value attributes of @ConditionalOnProperty are exclusive");
     *             return value.length > 0 ? value : name;
     *         }
     * </pre>
     *
     * @return
     */
    @Bean(name = "sparrow_default")
    @ConditionalOnMissingBean(DataSource.class)
    public ConnectionPool sparrow_default(ConnectionProxyContainer connectionProxyContainer) {
        log.info("sparrow_default datasource init ....");
        //兼容spring 注入方式
        return new ConnectionPool("sparrow_default", connectionProxyContainer);
    }
}
