package com.sparrow.spring.starter.druid.datasource;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;


@Slf4j
public class DruidCustomPasswordCallback extends DruidPasswordCallback {
    private String passwordKey;
    private Boolean debug = false;

    public DruidCustomPasswordCallback(String passwordKey, Boolean debug) {
        this.passwordKey = passwordKey;
        this.debug = debug;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String password = System.getenv(passwordKey);
        if(debug){
            log.debug("DruidCustomPasswordCallback setProperties, passwordKey:{}, password:{}", passwordKey, password);
        }
        if (!StringUtility.isNullOrEmpty(password)) {
            this.setPassword(password.toCharArray());
        }
    }
}
