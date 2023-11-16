package com.sparrow.spring.starter;

import com.sparrow.spring.starter.filter.ClientInformationFilter;
import com.sparrow.spring.starter.filter.FlashFilter;
import com.sparrow.spring.starter.resolver.ClientInfoArgumentResolvers;
import com.sparrow.spring.starter.resolver.LoginUserArgumentResolvers;
import com.sparrow.support.web.GlobalAttributeFilter;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class MvcConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Value("${sparrow.allowed_origins}")
    private String allowedOrigins;

    @Bean
    public FlashFilter flashFilter() {
        return new FlashFilter();
    }

    @Bean
    public SpringServletContainer springServletContainer() {
        return new SpringServletContainer();
    }

    @Bean
    public ClientInformationFilter clientInformationFilter() {
        return new ClientInformationFilter(-98, springServletContainer());
    }

    @Bean
    public ClientInfoArgumentResolvers clientInfoArgumentResolvers() {
        return new ClientInfoArgumentResolvers();
    }

    @Bean
    public LoginUserArgumentResolvers loginTokenArgumentResolvers() {
        return new LoginUserArgumentResolvers();
    }

    @Bean
    public GlobalAttributeFilter globalAttributeFilter() {
        return new GlobalAttributeFilter();
    }

    @Bean
    public FilterRegistrationBean<Filter> flashFilterBean() {
        FilterRegistrationBean<Filter> globalAttributeFilterBean = new FilterRegistrationBean<>();
        globalAttributeFilterBean.setFilter(flashFilter());
        globalAttributeFilterBean.addUrlPatterns("/*");
        globalAttributeFilterBean.setName("flashFilter");
        globalAttributeFilterBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return globalAttributeFilterBean;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }


    @Bean
    public FilterRegistrationBean<Filter> globalAttributeFilterBean() {
        FilterRegistrationBean<Filter> globalAttributeFilterBean = new FilterRegistrationBean<>();
        globalAttributeFilterBean.setFilter(globalAttributeFilter());
        globalAttributeFilterBean.addUrlPatterns("/*");
        globalAttributeFilterBean.setName("globalAttributeFilter");
        globalAttributeFilterBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return globalAttributeFilterBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> clientInformationFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(clientInformationFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("clientInformationFilter");
        filterRegistrationBean.setOrder(0);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

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

    /**
     * 兼容swagger 配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //必须以/结尾 垃圾。。。。
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
/**
 * 950518
 * 默认的WebMvcAutoConfiguration 中已存在，不需要重复配置
 * 但如果应用中配置了WebMvcConfigurationSupport 的子类实例，会将默认的覆盖，则需要手动添加
 *
 * if (!registry.hasMappingForPattern("/webjars/**")) {
 * 				customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
 * 						.addResourceLocations("classpath:/META-INF/resources/webjars/")
 * 						.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
 *  }
 */

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //前置,会和默认的WebMvcAutoConfiguration 配置叠加，导致所有动态请求异常s
        //registry.setOrder(-1);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.clientInfoArgumentResolvers());
        argumentResolvers.add(this.loginTokenArgumentResolvers());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (StringUtility.isNullOrEmpty(this.allowedOrigins)) {
            this.allowedOrigins = "*";
        }
        registry.addMapping("/**")
                .allowedOrigins(this.allowedOrigins)
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
