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
import com.sparrow.spring.filter.SparrowCorsFilter;
import com.sparrow.spring.resolver.ClientInfoArgumentResolvers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@AutoConfigureAfter(SparrowConfig.class)
public class MvcConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    public MvcConfigurerAdapter() {
        logger.info("MvcConfigurerAdapter init");
    }

    @Autowired
    private SparrowConfig sparrowConfig;

    /**
     * <pre>
     *     HttpMessageConvertersAutoConfiguration
     *     @Bean
     *     @ConditionalOnMissingBean
     *     public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
     *         return new HttpMessageConverters((Collection)converters.orderedStream().collect(Collectors.toList()));
     *     }
     * </pre>
     * 会自动配置 下文不需要
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /**
         * 只对输出结果对象提供转换
         * 基本数据类型直接返回Result,不支持封装转换
         */
//        converters.add(this.jsonMessageConverter);
//        converters.add(this.listJsonMessageConverter);
    }

    @Bean
    public ClientInfoArgumentResolvers clientInfoArgumentResolvers() {
        return new ClientInfoArgumentResolvers();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.clientInfoArgumentResolvers());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    /**
     * spring 跨域拦截器配置，如果在拦截器之前生效需要配置 CorsFilter
     *
     * @param registry
     * @see SparrowCorsFilter
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        if (this.sparrowConfig.getCors()==null||!this.sparrowConfig.getCors().isAllow()) {
//            logger.warn("cors config not found !");
//            return;
//        }
//        List<String> allowedOrigins = this.sparrowConfig.getCors().getAllowedOrigins();
//        String[] allowedOriginArray = new String[allowedOrigins.size()];
//        allowedOrigins.toArray(allowedOriginArray);
//        registry.addMapping("/**")
//                .allowedOriginPatterns(allowedOriginArray)
//                .allowCredentials(true)
//                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").maxAge(3600).allowCredentials(true);
    }
}
