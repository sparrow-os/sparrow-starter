//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sparrow.spring.starter.druid.datasource;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public class DruidDataSourceWrapper extends DruidDataSource implements InitializingBean {
    private DataSourceProperties basicProperties;

    public DruidDataSourceWrapper(DataSourceProperties basicProperties) {
        this.basicProperties = basicProperties;
    }

    public void afterPropertiesSet() {
        if (super.getUsername() == null) {
            super.setUsername(this.basicProperties.determineUsername());
        }

        if (super.getPassword() == null) {
            super.setPassword(this.basicProperties.determinePassword());
        }

        if (super.getUrl() == null) {
            super.setUrl(this.basicProperties.determineUrl());
        }

        if (super.getDriverClassName() == null) {
            super.setDriverClassName(this.basicProperties.getDriverClassName());
        }

    }
}
