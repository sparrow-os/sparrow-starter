package com.sparrow.spring.starter.test;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.annotation.*;

/**
 * 方便在没有Application启动类的场景下使用
 * 不需要额外创建Application类，只需要在测试类上添加注解即可
 * 如果找不到Bean 可以尝试添加
 *
 * @SpringBootApplication(scanBasePackages = "com.sparrow.*","other.package.*")
 */
@RunWith(SpringRunner.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
@SpringBootApplication
@TestPropertySource("classpath:application.properties")  //配置文件注入
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public @interface TestWithoutBootstrap {

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] scanBasePackages() default {};
    /**
     * @RunWith(SpringJUnit4ClassRunner.class)
     * 与
     * @RunWith(SpringRunner.class) 相同
     * @RunWith(SpringRunner.class) 是@RunWith(SpringJUnit4ClassRunner.class)
     * 的子类
     *
     * 工具类中单元测试使用 TestWithoutBootstrap 类，不需要使用
     * @SpringBootTest(classes = {Application.class})
     * 但需要配置
     *
     * @SpringBootApplication(scanBasePackages = "com.sparrow.*")
     * @TestPropertySource("classpath:application.properties")  //配置文件注入
     * 自定义监听器后，需要配置依赖注入监听器
     * @TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
     */
}
