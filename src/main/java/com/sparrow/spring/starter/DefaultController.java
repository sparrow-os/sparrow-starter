package com.sparrow.spring.starter;

import com.sparrow.spring.starter.autoconfiguration.TemplateEngineAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class DefaultController {
    /**
     * |||* 与 /** 的区别
     * <p>
     * /* 是所有文件夹，不含子文件夹
     * <p>
     * ** 的意思是所有文件夹及里面的子文件夹
     *
     * <pre>
     *     Spring MVC设定的Url Mapping有几种情况：
     *     WebMvcPropertySourcedRequestMappingHandlerMapping 最高优先级
     *
     * （1）application.properties配置的静态资源路径：spring.mvc.static-locations=${user.home}/webroot/
     * （2）Controller定义的路径：
     *    1.全路径：@GetMapping("/upload/test.html")
     *    2.正则路径：@GetMapping("/**.html")
     *
     * （3）通过ResourceHandlerRegistry 注册的路径：
     * <pre>
     * public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * registry.addResourceHandler("/upload/**").addResourceLocations("file:/webroot/upload");
     * </pre>
     * 这些路径在Spring加载后是有优先级的，默认我理解应当是按Spring加载顺序来定义优先级，即先加载的优先级低。
     * <p>
     * 默认应当为（1）（3）（2）加载顺序，即（2）的优先级最高，（1）最低。
     * <p>
     * 访问 /upload/test.html 如果1 / 2 / 3 中都有匹配时，则会用（2）中的1
     * <p>
     * 访问 /upload/test2222.html如果1 / 2 / 3 中都有匹配时，则会用（2）中的2
     * <p>
     * 其中（3）的优先级可以改，可以定为高优先级，通过设定registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
     * 实现
     * <p>
     * 此时再次访问访问 /upload/test.html如果1 / 2 / 3 中都有匹配时，则会使用（3）的规则
     * </pre >
     * @see TemplateEngineAutoConfiguration
     */
    @GetMapping("/**")
    @Deprecated
    public ModelAndView all(HttpServletRequest request) {
        return new ModelAndView(request.getServletPath());
    }

    /**
     * 与
     * package org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
     * 冲突，解决办法
     * 1. 重写url
     * 2. server.error.path 修改默认配置/
     *
     * @return
     */
//    @GetMapping("/error")
//    public ModelAndView error() {
//        return new ModelAndView("/error");
//    }
    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
