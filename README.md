# Sparrow与spring boot 相关通用配置

```
package org.springframework.boot.autoconfigure.jdbc;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@ConditionalOnMissingBean(type = "io.r2dbc.spi.ConnectionFactory")
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ DataSourcePoolMetadataProvidersConfiguration.class, DataSourceInitializationConfiguration.class })
public class DataSourceAutoConfiguration {

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
    If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
    If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).

```

## 关于数据源
```
     <dependency>
          <groupId>com.alibaba</groupId>
          <artifactId>druid-spring-boot-starter</artifactId>
          <version>${druid.version}</version>
          <optional>true</optional>
      </dependency>
          依赖方可选 
          如果不加<optional>,那么上层依赖不需要数据库
          需要手动exclude
```

不需要依赖上方starer ，依赖后自动加载 并且exclude = {DataSourceAutoConfiguration.class}也无法排除<br/>
如下所示:

```
@SpringBootApplication(scanBasePackages = "com.sparrow.*", exclude = {DataSourceAutoConfiguration.class})
```

需要 exclude = {DataSourceAutoConfiguration.class,DruidDataSourceAutoConfigure.class} 排除<br/>
如下所示:

```
@SpringBootApplication(scanBasePackages = "com.sparrow.*", exclude = {DataSourceAutoConfiguration.class,DruidDataSourceAutoConfigure.class})
```


拦截器 controller 可以实现基类，由业务层继承复用


## Spring boot bean初始化机制
### spring.factories
使用 spring.factories是springboot加载bean的一种方式，通常用于自动装配环境配置相关的类，和Configuration注解有异曲同工之妙。

其机制和java的SPI机制类似，不同的是其配置规则 : spring.factories以key，value的键值对列表存在。value是类的完全限定类名。类之间用逗号（，）分隔，后面跟着反斜杠（\）和换行符，以提高可读性。

org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.example.AutoConfigurationClass1,\
com.example.AutoConfigurationClass2

### @Configuration + @ComponentScan
@Configuration顾名思义就是我们的环境配置，标记SpringBoot中的配置类。其一般与其他Spring的相关配置一起使用。内部的话就是我们需要注入到spring容器的bean。

与@Conditiona簇的注解一起使用，用于在标识该类在满足某种规则后下生效。
在配置类中我们也可以添加@import注解导入某些类。
二者使用场景：
一般我们的配置类如果在启动类包路径下的话，SpringBoot可以自行装配，无需其他配置去扫描发现。但是如果不是的话，则需要配置 @ComponentScan或者 spring.factories文件了。一般我们开发一个框架的话，推荐使用spring.factories的方式，这样使用者导入相应的jar包(一般是spring-starter包)即可自动装配相应的bean。

### 装配顺序
Spring Boot的自动配置均是通过spring.factories来指定的，它的优先级最低（执行时机是最晚的）；通过扫描进来的优先级是最高的。

spring.factories 的优先级最低，最后加载
注意:必须添加ConditionalOnMissingBean注解，否则无法被上层业务覆盖
Spring Boot的自动配置是通过@EnableAutoConfiguration注解驱动的，默认是开启状态。你也可以通过spring.boot.enableautoconfiguration = false来关闭它，回退到Spring Framework时代。

### @AutoConfigureBefore 控制优先级
DruidDataSourceAutoConfiguration 强制在以下类之前先装配
@AutoConfigureBefore({SparrowDataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
