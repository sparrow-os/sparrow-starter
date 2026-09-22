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
package com.sparrow.spring.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.annotation.*;

/**
 * 方便在没有Application启动类的场景下使用
 * 不需要额外创建Application类，只需要在测试类上添加注解即可
 * 如果找不到Bean 可以尝试添加
 *
 * @SpringBootApplication(scanBasePackages = "com.sparrow.*","other.package.*")
 */
@ExtendWith(SpringExtension.class)
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
