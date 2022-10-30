package com.sparrow.spring.starter;

import com.sparrow.spring.starter.filter.ClientInformationFilter;
import com.sparrow.spring.starter.filter.LoginTokenFilter;
import com.sparrow.spring.starter.resolver.ClientInfoArgumentResolvers;
import com.sparrow.spring.starter.resolver.LoginTokenArgumentResolvers;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Inject
    private LoginTokenFilter loginTokenFilter;

    @Inject
    private ClientInformationFilter clientInformationFilter;

    @Inject
    private VOJsonMessageConverter jsonMessageConverter;

    @Inject
    private VOListJsonMessageConverter listJsonMessageConverter;

    @Inject
    private ClientInfoArgumentResolvers clientInfoArgumentResolvers;

    @Inject
    private LoginTokenArgumentResolvers loginTokenArgumentResolvers;

    @Bean
    public FilterRegistrationBean<Filter> clientInformationFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(clientInformationFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("clientInformationFilter");
        filterRegistrationBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> loginTokenFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(loginTokenFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("loginTokenFilter");
        filterRegistrationBean.setOrder(0);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Override public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /**
         * 只对输出结果对象提供转换
         * 基本数据类型直接返回Result,不支持封装转换f
         */
        converters.add(this.jsonMessageConverter);
        converters.add(this.listJsonMessageConverter);
    }

    /**
     * 配置静态访问资源
     * <p>
     * http://localhost:8000/s/static.html
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.clientInfoArgumentResolvers);
        argumentResolvers.add(this.loginTokenArgumentResolvers);
    }
}
