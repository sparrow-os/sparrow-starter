package com.sparrow.spring.starter.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication(scanBasePackages = "com.sparrow.*")
@TestPropertySource("classpath:application.properties")  //配置文件注入
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public @interface TestWithoutBootstrap {
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
     */
}
