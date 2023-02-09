package com.sparrow.spring.starter;

import com.sparrow.spring.starter.Interceptor.ParameterInterceptor;
import com.sparrow.spring.starter.filter.ClientInformationFilter;
import com.sparrow.spring.starter.filter.FlashFilter;
import com.sparrow.spring.starter.filter.GlobalAttributeFilter;
import com.sparrow.spring.starter.filter.LoginUserFilter;
import com.sparrow.spring.starter.message.converter.ListJsonMessageConverter;
import com.sparrow.spring.starter.message.converter.VOJsonMessageConverter;
import com.sparrow.spring.starter.resolver.ClientInfoArgumentResolvers;
import com.sparrow.spring.starter.resolver.LoginUserArgumentResolvers;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Inject
    private LoginUserFilter loginTokenFilter;

    @Inject
    private FlashFilter flashFilter;

    @Inject
    private ClientInformationFilter clientInformationFilter;

    @Inject
    private GlobalAttributeFilter globalAttributeFilter;

    @Inject
    private VOJsonMessageConverter jsonMessageConverter;

    @Inject
    private ListJsonMessageConverter listJsonMessageConverter;

    @Inject
    private ClientInfoArgumentResolvers clientInfoArgumentResolvers;

    @Inject
    private LoginUserArgumentResolvers loginTokenArgumentResolvers;

    @Inject
    private ParameterInterceptor parameterInterceptor;

    @Bean
    public FilterRegistrationBean<Filter> flashFilterBean() {
        FilterRegistrationBean<Filter> globalAttributeFilterBean = new FilterRegistrationBean<>();
        globalAttributeFilterBean.setFilter(flashFilter);
        globalAttributeFilterBean.addUrlPatterns("/*");
        globalAttributeFilterBean.setName("flashFilter");
        globalAttributeFilterBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return globalAttributeFilterBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> globalAttributeFilterBean() {
        FilterRegistrationBean<Filter> globalAttributeFilterBean = new FilterRegistrationBean<>();
        globalAttributeFilterBean.setFilter(globalAttributeFilter);
        globalAttributeFilterBean.addUrlPatterns("/*");
        globalAttributeFilterBean.setName("globalAttributeFilter");
        globalAttributeFilterBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return globalAttributeFilterBean;
    }

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
     * 兼容swagger 配置
     *
     * @param registry
     */
    @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //配置静态访问资源
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.clientInfoArgumentResolvers);
        argumentResolvers.add(this.loginTokenArgumentResolvers);
    }

    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.parameterInterceptor);
    }
}
