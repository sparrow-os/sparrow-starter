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

