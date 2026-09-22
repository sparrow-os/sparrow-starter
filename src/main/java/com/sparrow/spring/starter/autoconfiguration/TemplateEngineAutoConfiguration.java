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

import com.sparrow.spring.config.SparrowConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;

import java.util.Set;

@AutoConfigureAfter({SparrowConfig.class, ThymeleafProperties.class})
@ConditionalOnClass(TemplateEngine.class)
@Slf4j
public class TemplateEngineAutoConfiguration implements WebMvcConfigurer {
    public TemplateEngineAutoConfiguration() {
        log.info("TemplateEngineAutoConfiguration init");
    }

    @Autowired
    private SparrowConfig sparrowConfig;
    @Autowired
    private ThymeleafProperties thymeleafProperties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String prefix = thymeleafProperties.getPrefix();
        //String suffix = thymeleafProperties.getSuffix();
        Set<String> viewNames = sparrowConfig.getMvc().getAutoMappingViewNames();
        for (String viewName : viewNames) {
            registry.addViewController(viewName).setViewName(viewName);
        }
    }
}
