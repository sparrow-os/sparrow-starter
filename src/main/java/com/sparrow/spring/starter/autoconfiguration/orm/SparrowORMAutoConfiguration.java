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
import org.springframework.context.annotation.Configuration;

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
