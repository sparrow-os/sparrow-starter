https://sparrowzoo.feishu.cn/docx/WT7AdTMdEoIeD1xgMhzcg7cOnTg


# Sparrow 功能及职责分析
# 配置Config
Sparrow Config

#  容器
- EnumContainer 容器
- SparrowContainer 容器的Spring 实现SpringContainer
```azure
直接用JDK 的SPI 使用Spring 容器，在业务代码不依赖spring 框架时使用
比如DDD 或者整洁架构中的domain 核心业务层，不直接依赖spring框架，但可以读spring 的bean
需要在main 依赖sparrow-starter
```
- Spring Context 上下文
- Spring req rep 容器

# MVC Web
- AOP拦截所有Controller
  拦截所有Controller 并将结果转换为Result 对象
  GlobalExceptionHandler

- 默认Controller
DefaultController

# 数据源
Druid 的密码回调

# Filter

- AccessMonitorFilter

访问QPS相关指标监控

- ClientInformationFilter

客户端信息收集

- FlashFilter
```
spring 实现的flash 是url 变化后自动清除session*
本方案支持中间跳转状态保持功能
ModelAndViewUtils 为实现该功能提供工具类
```
- SparrowCorsFilter
- SpringGlobalAttributeFilter

# 拦截器
- Flash 参数 自定义框架使用
- Mybatis 参数拦截器

# MQ
- Spring MQ Handler 基类

# Mybatis
状态字段Handler

# Redis
- Redis 流控
- Redis 验证码
```azure
http://localhost:8888/captcha 
自动初始化验证码的servlet
```

# Resolver
- ClientInfoArgumentResolvers
- LoginUserArgumentResolver 在authenticator-core 中






















# 

# Interceptor

## FlashParamPrepareAspect

## MybatisInterceptor



# 容器相关



