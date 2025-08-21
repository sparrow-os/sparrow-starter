package com.sparrow.spring.starter.autoconfiguration;

import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.filter.SparrowCorsFilter;
import com.sparrow.spring.starter.resolver.ClientInfoArgumentResolvers;
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
     * spring 跨域拦截器配置，在filter 中返回的情况无法生效
     * 如果在拦截器之前生效需要配置 CorsFilter
     *
     * @param registry
     * @see SparrowCorsFilter
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (this.sparrowConfig.getCors()==null||!this.sparrowConfig.getCors().isAllow()) {
            logger.warn("cors config not found !");
            return;
        }
        List<String> allowedOrigins = this.sparrowConfig.getCors().getAllowedOrigins();
        String[] allowedOriginArray = new String[allowedOrigins.size()];
        allowedOrigins.toArray(allowedOriginArray);
        registry.addMapping("/**")
                .allowedOrigins(allowedOriginArray)
                .allowCredentials(true)
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").maxAge(3600).allowCredentials(true);
    }
}
