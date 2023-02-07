package com.sparrow.spring.starter;

import com.alibaba.druid.pool.DruidDataSource;
import com.sparrow.datasource.ConnectionPool;
import com.sparrow.datasource.DataSourceFactory;
import com.sparrow.datasource.DataSourceFactoryImpl;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(SparrowConfig.class)
public class SparrowDataSourceAutoConfiguration {

    @Inject
    private SparrowConfig sparrowConfig;

    /**
     * <pre>
     *     private String[] getNames(Map<String, Object> annotationAttributes) {
     *             String[] value = (String[])((String[])annotationAttributes.get("value"));
     *             String[] name = (String[])((String[])annotationAttributes.get("name"));
     *             //name 或value必须有一个
     *             Assert.state(value.length > 0 || name.length > 0, "The name or value attribute of @ConditionalOnProperty must be specified");
     *             //name 和value 是互斥的
     *             Assert.state(value.length == 0 || name.length == 0, "The name and value attributes of @ConditionalOnProperty are exclusive");
     *             return value.length > 0 ? value : name;
     *         }
     * </pre>
     *
     * @return
     */
    @Bean(name = "sparrow_default")
    @ConditionalOnProperty(prefix = "sparrow", name = "ds", havingValue = "sparrow")
    public DataSource sparrow_default(DataSourceFactory dataSourceFactory) {
        ConnectionPool connectionPool = new ConnectionPool();
        connectionPool.setDataSourceFactory(dataSourceFactory);
        return connectionPool;
    }

    @Bean(name = "sparrow_default")
    @ConditionalOnProperty(prefix = "sparrow", name = "ds", havingValue = "druid")
    @ConditionalOnClass(DruidDataSource.class)
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(this.sparrowConfig.getUsername());
        druidDataSource.setPassword(this.sparrowConfig.getPassword());
        druidDataSource.setUrl(this.sparrowConfig.getUrl());
        druidDataSource.setDriverClassName(this.sparrowConfig.getDriverClassName());
        druidDataSource.setInitialSize(8);
        druidDataSource.setMaxActive(8);
        druidDataSource.setBreakAfterAcquireFailure(true);
        return druidDataSource;
    }

    @Bean
    public DataSourceFactoryImpl dataSourceFactory() {
        return new DataSourceFactoryImpl("sparrow_default");
    }
}