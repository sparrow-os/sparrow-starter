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
package com.sparrow.spring.starter.autoconfiguration.orm;

import com.sparrow.datasource.ConnectionContextHolder;
import com.sparrow.datasource.DataSourceFactory;
import com.sparrow.orm.datasource.DataSourceFactoryImpl;
import com.sparrow.orm.datasource.DefaultDataSourceDispatcher;
import com.sparrow.orm.template.impl.DBORMTemplate;
import com.sparrow.orm.transaction.ConnectionContextHolderImpl;
import com.sparrow.orm.transaction.SparrowTransactionManager;
import com.sparrow.protocol.dao.DataSourceDispatcher;
import com.sparrow.transaction.TransactionManager;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


@ConditionalOnClass(DBORMTemplate.class)
@AutoConfigureBefore(DataSourceTransactionManagerAutoConfiguration.class)
public class SparrowORMAutoConfiguration {

    /**
     * return new DataSourceFactoryImpl("sparrow_default,user_default");
     * 业务可以自定义多个数据源
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DataSourceFactoryImpl.class)
    @ConditionalOnBean(DataSource.class)
    public DataSourceFactory dataSourceFactory() {
        return new DataSourceFactoryImpl();
    }

    @Bean
    public ConnectionContextHolder connectionContextHolder() {
        return new ConnectionContextHolderImpl();
    }

    @Bean
    @ConditionalOnBean(DataSourceFactory.class)
    public TransactionManager sparrowTransactionManager(ConnectionContextHolder connectionContextHolder,
                                                        DataSourceFactory dataSourceFactory) {
        return new SparrowTransactionManager(connectionContextHolder, dataSourceFactory);
    }

    @Bean
    @ConditionalOnBean(DataSourceFactory.class)
    public DataSourceDispatcher dataSourceDispatcher(DataSourceFactory dataSourceFactory) {
        return new DefaultDataSourceDispatcher(dataSourceFactory);
    }
}
