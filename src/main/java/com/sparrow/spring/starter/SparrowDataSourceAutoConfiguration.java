package com.sparrow.spring.starter;

import com.sparrow.datasource.ConnectionPool;
import com.sparrow.datasource.DataSourceFactory;
import com.sparrow.datasource.DataSourceFactoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnBean(SparrowConfig.class)
@ConditionalOnMissingBean(DataSource.class)
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
    public DataSource sparrow_default(DataSourceFactory dataSourceFactory) {
        ConnectionPool connectionPool = new ConnectionPool();
        connectionPool.setDataSourceFactory(dataSourceFactory);
        return connectionPool;
    }

    /**
     * 只配置一个数据源，此处为框架学习使用，生产环境的druid配置比较复杂，由业务自行配置
     * sparrow.ds=sparrow 不再生效
     */
//    @Bean(name = "sparrow_default")
//    @ConditionalOnProperty(prefix = "sparrow", name = "ds", havingValue = "druid")
//    @ConditionalOnClass(DruidDataSource.class)
//    public DruidDataSource druidDataSource() {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        return druidDataSource;
//    }
    @Bean
    public DataSourceFactoryImpl dataSourceFactory() {
        return new DataSourceFactoryImpl("sparrow_default");
    }
}
