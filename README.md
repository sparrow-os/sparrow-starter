# Sparrow 功能及职责分析

> 📖 设计文档：[飞书在线文档](https://sparrowzoo.feishu.cn/docx/WT7AdTMdEoIeD1xgMhzcg7cOnTg)
>
> 📖 项目源码分析说明：[项目说明](./项目说明.md)

## 配置 Config

- `Sparrow Config`：全局配置入口（前缀 `sparrow.*`）。

## 容器

- **EnumContainer 容器**：枚举容器。
- **SpringContainer 容器**：SparrowContainer 容器的 Spring 实现。

  > 通过 JDK 的 SPI 直接使用 Spring 容器，适用于业务代码不依赖 Spring 框架的场景，例如 DDD 或整洁架构中的 domain 核心业务层。核心层不直接依赖 Spring，但可以读取 Spring 管理的 Bean。需在 `main` 依赖 `sparrow-starter`。

- **Spring Context 上下文**。
- **Spring request/response 容器**：提供当前请求与响应。

## MVC Web

- **AOP 拦截所有 Controller**：拦截所有 Controller 并将结果转换为 `Result` 对象（`GlobalExceptionHandler`）。
- **默认 Controller**：`DefaultController`。

## 数据源

- **Druid 的密码回调**：支持数据源密码解密。

## Filter

- **AccessMonitorFilter**：访问 QPS 相关指标监控。
- **ClientInformationFilter**：客户端信息收集。
- **FlashFilter**：

  > Spring 原生的 flash 是 URL 变化后自动清除 session；本方案支持中间跳转状态保持功能，`ModelAndViewUtils` 为实现该功能提供工具类。

- **SparrowCorsFilter**：跨域支持。
- **SpringGlobalAttributeFilter**：全局属性注入。

## 拦截器

- **Flash 参数**：自定义框架使用。
- **MyBatis 参数拦截器**：MyBatis 参数处理。

## MQ

- **Spring MQ Handler 基类**：`AbstractSpringMQHandler`。

## MyBatis

- **状态字段 Handler**：`RecordStateTypeHandler`（状态枚举与数据库记录状态互转）。

## Redis

- **Redis 流控**：基于 Redis 的限流。
- **Redis 验证码**：

  ```text
  http://localhost:8888/captcha
  ```

  自动初始化验证码的 Servlet（默认 Session 实现，可切 Redis）。

## Resolver

- `ClientInfoArgumentResolvers`：Controller 参数自动注入客户端信息。
- `LoginUserArgumentResolver`：位于 `authenticator-core` 中。