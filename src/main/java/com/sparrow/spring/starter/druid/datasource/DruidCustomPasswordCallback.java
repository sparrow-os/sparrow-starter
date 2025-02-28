package com.sparrow.spring.starter.druid.datasource;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.sparrow.utility.StringUtility;

import java.util.Properties;


public class DruidCustomPasswordCallback extends DruidPasswordCallback {
    private String passwordKey="mysql_sparrow_password";
    public DruidCustomPasswordCallback(String passwordKey) {
        this.passwordKey = passwordKey;
    }
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String password = System.getenv(passwordKey);
        if (!StringUtility.isNullOrEmpty(password)) {
            this.setPassword(password.toCharArray());
        }
    }
}
