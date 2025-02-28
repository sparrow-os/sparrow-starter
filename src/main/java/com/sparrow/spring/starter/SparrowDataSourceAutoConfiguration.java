package com.sparrow.spring.starter;

import com.sparrow.datasource.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(ConnectionContextHolder.class)
public class SparrowDataSourceAutoConfiguration {
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
    public DataSource sparrow_default() {
        return new ConnectionPool();
    }

    /**
     * return new DataSourceFactoryImpl("sparrow_default,user_default");
     * 业务可以自定义多个数据源
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DataSourceFactoryImpl.class)
    @ConditionalOnBean(DataSource.class)
    public DataSourceFactoryImpl dataSourceFactory() {
        return new DataSourceFactoryImpl("sparrow_default");
    }

    @Bean
    public ConnectionContextHolder connectionContextHolder(DataSourceFactoryImpl dataSourceFactory) {
        return new ConnectionContextHolderImpl(dataSourceFactory);
    }
}
