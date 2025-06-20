package com.sparrow.spring.starter;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.druid.datasource.DruidCustomPasswordCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;
import javax.sql.DataSource;

@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore({SparrowDataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@AutoConfigureAfter(SparrowConfig.class)
@Slf4j
public class DruidDataSourceAutoConfiguration {

    @Autowired
    private SparrowConfig sparrowConfig;

    @Inject
    public DruidCustomPasswordCallback passwordCallback() {
        if(this.sparrowConfig.getDataSource()==null){
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
