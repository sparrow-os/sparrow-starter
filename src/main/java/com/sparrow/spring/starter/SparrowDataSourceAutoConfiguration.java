package com.sparrow.spring.starter;

import com.sparrow.datasource.ConnectionPool;
import com.sparrow.datasource.DataSourceFactory;
import com.sparrow.datasource.DataSourceFactoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(SparrowConfig.class)
@ConditionalOnBean(SparrowConfig.class)
public class SparrowDataSourceAutoConfiguration {

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
    @ConditionalOnClass(ConnectionPool.class)
    @ConditionalOnMissingBean(ConnectionPool.class)
    public DataSource sparrow_default(DataSourceFactory dataSourceFactory) {
        ConnectionPool connectionPool = new ConnectionPool();
        connectionPool.setDataSourceFactory(dataSourceFactory);
        return connectionPool;
    }


    @Bean
    @ConditionalOnClass(DataSourceFactoryImpl.class)
    @ConditionalOnMissingBean(DataSourceFactoryImpl.class)
    public DataSourceFactoryImpl dataSourceFactory() {
        return new DataSourceFactoryImpl("sparrow_default");
    }
}
