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
package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.spring.config.SparrowOpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;


@EnableConfigurationProperties(SparrowOpenApiProperties.class)
@ConditionalOnClass(OpenAPI.class)
@Slf4j
public class OpenApiAutoConfiguration {
    /**
     * 注册 OpenAPI 元信息。
     *
     * <ul>
     *   <li>{@link ConditionalOnMissingBean}：若宿主项目已自定义 {@link OpenAPI} Bean，
     *       则本 Bean 不再注册，宿主配置优先。</li>
     *   <li>{@link ConditionalOnProperty}：通过 {@code sparrow.passport.openapi.enabled=false}
     *       可关闭本 starter 提供的元信息。</li>
     * </ul>
     */
    @Bean
    @ConditionalOnMissingBean(name = "sparrowOpenAPI")
    @ConditionalOnProperty(prefix = "sparrow.openapi", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    public OpenAPI sparrowOpenAPI(SparrowOpenApiProperties properties) {
        log.info("init open api bean");
        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion());

        if (StringUtils.hasText(properties.getTermsOfService())) {
            info.setTermsOfService(properties.getTermsOfService());
        }

        SparrowOpenApiProperties.Contact contact = properties.getContact();
        if (contact != null) {
            info.contact(new Contact()
                    .name(contact.getName())
                    .url(contact.getUrl())
                    .email(contact.getEmail()));
        }

        SparrowOpenApiProperties.License license = properties.getLicense();
        if (license != null && (StringUtils.hasText(license.getName()) || StringUtils.hasText(license.getUrl()))) {
            info.license(new License().name(license.getName()).url(license.getUrl()));
        }

        return new OpenAPI().info(info);
    }
}
