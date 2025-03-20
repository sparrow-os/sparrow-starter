package com.sparrow.spring.starter;

import com.sparrow.spring.starter.config.SparrowConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;

import java.util.List;

@Configuration
@AutoConfigureAfter({SparrowConfig.class, ThymeleafProperties.class})
@ConditionalOnClass(TemplateEngine.class)
public class TemplateEngineAutoconfiguration implements WebMvcConfigurer {
    public TemplateEngineAutoconfiguration() {
        System.out.println("TemplateEngineAutoconfiguration");
    }

    @Autowired
    private SparrowConfig sparrowConfig;
    @Autowired
    private ThymeleafProperties thymeleafProperties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String prefix = thymeleafProperties.getPrefix();
        //String suffix = thymeleafProperties.getSuffix();
        List<String> viewNames = sparrowConfig.getMvc().getAutoMappingViewNames();
        for (String viewName : viewNames) {
            registry.addViewController(viewName).setViewName(viewName);
        }
    }
}
