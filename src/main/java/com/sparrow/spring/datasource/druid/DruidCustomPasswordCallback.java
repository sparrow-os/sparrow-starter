/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.spring.datasource.druid;

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
        if (debug) {
            log.debug("DruidCustomPasswordCallback setProperties, passwordKey:{}, password:{}", passwordKey, password);
        }
        if (!StringUtility.isNullOrEmpty(password)) {
            this.setPassword(password.toCharArray());
        }
    }
}
