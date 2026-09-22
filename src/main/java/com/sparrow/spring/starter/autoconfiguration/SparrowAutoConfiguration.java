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

import com.sparrow.constant.CacheNames;
import com.sparrow.constant.Config;
import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.StringSoftExpirableCache;
import com.sparrow.datasource.DatasourceConfigReader;
import com.sparrow.email.EmailSender;
import com.sparrow.image.ImageExtractorRegistry;
import com.sparrow.io.FileService;
import com.sparrow.io.impl.JDKFileService;
import com.sparrow.mq.DefaultQueueHandlerMappingContainer;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.protocol.BeanCopier;
import com.sparrow.spring.Interceptor.FlashParamPrepareAspect;
import com.sparrow.spring.container.SpringContext;
import com.sparrow.spring.container.SpringServletContainer;
import com.sparrow.spring.config.SparrowConfig;
import com.sparrow.spring.filter.monitor.Monitor;
import com.sparrow.support.IpSupport;
import com.sparrow.support.ip.SparrowIpSupport;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.SparrowBeanCopier;
import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties(SparrowConfig.class)
@Slf4j
public class SparrowAutoConfiguration {
    public SparrowAutoConfiguration() {
        log.info("Sparrow Auto Configuration INIT");
    }

    @Autowired
    private SparrowConfig sparrowConfig;

    @Bean(name = CacheNames.ACTION_URL_CACHE)
    @ConditionalOnMissingBean(name = CacheNames.ACTION_URL_CACHE)
    public Cache<String, String> actionUrlCache() {
        return new StringSoftExpirableCache(CacheNames.ACTION_URL_CACHE, sparrowConfig.getMvc().getActionUrlCacheExpiredSeconds());
    }

    @Bean
    @ConditionalOnMissingBean(IpSupport.class)
    public IpSupport ipSupport() {
        return new SparrowIpSupport();
    }

    @Bean
    @ConditionalOnMissingBean(Monitor.class)
    public Monitor monitor(IpSupport ipSupport) {
        return new Monitor(ipSupport);
    }

    @Bean
    @ConditionalOnMissingBean(BeanCopier.class)
    public BeanCopier beanCopier() {
        return new SparrowBeanCopier();
    }

    @Bean
    @ConditionalOnMissingBean(WebConfigReader.class)
    public WebConfigReader webConfigReader() {
        return this.sparrowConfig.getMvc();
    }

    @Bean
    @ConditionalOnMissingBean(DatasourceConfigReader.class)
    public DatasourceConfigReader datasourceConfigReader() {
        return sparrowConfig.getDataSource();
    }


    @Bean
    @ConditionalOnMissingBean(CookieUtility.class)
    public CookieUtility cookieUtility() {
        return new CookieUtility();
    }

    @Bean
    @ConditionalOnMissingBean(SpringServletContainer.class)
    public SpringServletContainer springServletContainer() {
        return new SpringServletContainer();
    }

    @Bean
    @ConditionalOnMissingBean(FlashParamPrepareAspect.class)
    public FlashParamPrepareAspect flashParamPrepareAspect() {
        return new FlashParamPrepareAspect();
    }

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService fileService() {
        return new JDKFileService();
    }


    @Bean
    @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "sparrow.email", name = "enabled", havingValue = "true")
    public EmailSender emailSender() {
        SparrowConfig.Email email = sparrowConfig.getEmail();
        String emailPassword = System.getenv(Config.EMAIL_PASSWORD);
        if (email.getDebugPassword()) {
            log.info("online password {}", emailPassword);
        }
        if (StringUtility.isNullOrEmpty(emailPassword)) {
            emailPassword = email.getPassword();
        }
        log.info("final password {}", emailPassword);
        return new EmailSender(email.getLocalAddress(), email.getHost(), email.getFrom(), email.getUsername(), emailPassword);
    }

    @Bean
    @ConditionalOnMissingBean(ImageExtractorRegistry.class)
    public ImageExtractorRegistry imageExtractorRegistry() {
        return new ImageExtractorRegistry();
    }

    public static class SpringContextAutoConfiguration {
        public SpringContextAutoConfiguration() {
            log.info("SpringContextAutoConfiguration INIT");
        }

        @Bean
        public SpringContext springContext() {
            return new SpringContext();
        }
    }

    @Bean
    @ConditionalOnMissingBean(DefaultQueueHandlerMappingContainer.class)
    public EventHandlerMappingContainer eventHandlerMappingContainer() {
        return new DefaultQueueHandlerMappingContainer();
    }
}
