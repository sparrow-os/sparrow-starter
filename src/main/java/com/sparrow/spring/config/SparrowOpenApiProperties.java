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
package com.sparrow.spring.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Passport starter 的 OpenAPI 元信息配置。
 *
 * <p>统一使用 {@code sparrow.passport.openapi.*} 前缀，避免与 springdoc 官方
 * {@code springdoc.*} 以及宿主应用的配置命名空间冲突，从而实现 starter 与宿主项目的隔离。
 */
@ConfigurationProperties(prefix = "sparrow.openapi")
@Data
@NoArgsConstructor
public class SparrowOpenApiProperties {
    /**
     * 是否注册本 starter 提供的 OpenAPI 元信息 Bean。
     * 关闭后，若宿主未自定义，springdoc 会回退到默认的空 Info。
     */
    private boolean enabled = true;
    private String title = "Sparrowzoo";
    private String description = "Sparrowzoo";
    private String version = "1.0";
    private String termsOfService;
    private final Contact contact = new Contact();
    private final License license = new License();



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        private String name = "harry";
        private String url = "http://www.sparrowzoo.com";
        private String email = "zh_harry@163.com";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class License {
        private String name;
        private String url;
    }
}
