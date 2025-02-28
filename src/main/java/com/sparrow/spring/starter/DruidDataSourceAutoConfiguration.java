package com.sparrow.spring.starter;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.sparrow.spring.starter.druid.datasource.DruidCustomPasswordCallback;
import com.sparrow.spring.starter.druid.datasource.DruidDataSourceWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore({SparrowDataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class DruidDataSourceAutoConfiguration {
    @Value("${spring.datasource.password-key}")
    private String passwordKey;

    @Autowired
    private DataSourceProperties basicProperties;

    @Bean
    public DruidCustomPasswordCallback passwordCallback() {
        return new DruidCustomPasswordCallback(passwordKey);
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
        DruidDataSource druidDataSource = new DruidDataSourceWrapper(basicProperties);
        druidDataSource.setPasswordCallback(passwordCallback());
        return druidDataSource;
    }
}
