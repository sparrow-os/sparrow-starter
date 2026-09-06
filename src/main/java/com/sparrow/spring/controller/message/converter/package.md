会将所有的controller 返回结果进行包装
对自身业务。比如swagger 会产生影响

**# swagger 前端报错

```
e.ajax({
                    url: e.url,
                    type: "get",
                    timeout: 2e4,
                    dataType: "json",
                    headers: t
                }, (function(t) {
                    e.springdoc ? e.analysisSpringDocOpenApiGroupSuccess(t) : e.analysisGroupSuccess(t),
                    e.createGroupElement()
                }
                ), (function(t) {
                    S.a.error("Knife4j文档请求异常"),
                    e.error(t)
                }
                ))
```
# 异常
```
app.23f8b31d.js:1 TypeError: n.forEach is not a function
    at Oe.analysisGroupSuccess (app.23f8b31d.js:1:173354)
    at app.23f8b31d.js:1:171658
    at app.23f8b31d.js:1:249418
```

# ResponseBodyAdvice 与 HttpMessageConverter 的区别
1. ‌功能定位‌
‌ResponseBodyAdvice‌
是 Spring MVC 提供的全局性扩展接口，用于在控制器方法返回数据后、数据写入 HTTP 响应体前，对响应数据进行统一处理（如修改数据格式、添加统一封装逻辑等）。例如，将大写下划线字段转换为驼峰命名‌13。
‌HttpMessageConverter‌
是 Spring 中负责数据序列化/反序列化的核心组件，用于将 Java 对象转换为 HTTP 消息体（如 JSON、XML），或反向转换。例如，将 @ResponseBody 修饰的返回值序列化为 JSON 字符串‌23。
2. ‌触发时机与作用范围‌
‌ResponseBodyAdvice‌
在控制器方法执行后、HttpMessageConverter 的 write() 方法执行前触发。其拦截范围由 @ControllerAdvice 的 basePackages 或 annotations 参数控制，支持全局或特定包下的控制器‌13。
‌HttpMessageConverter‌
在请求处理管道的序列化阶段触发，根据请求的 Content-Type 和 Accept 头选择匹配的转换器，作用范围与媒体类型和配置的转换器类型相关‌12。
3. ‌典型应用场景‌
‌ResponseBodyAdvice‌
全局统一响应格式（如封装为 {"code": 200, "data": ...}）‌34。
日志记录（如记录响应体内容）‌15。
数据格式转换（如字段名规则转换）‌1。
‌HttpMessageConverter‌
序列化控制器返回的 Java 对象为 JSON/XML（如使用 MappingJackson2HttpMessageConverter）‌23。
反序列化 HTTP 请求体为 Java 对象（如解析 @RequestBody 参数）‌23。
4. ‌实现方式‌
‌ResponseBodyAdvice‌
需实现 ResponseBodyAdvice 接口，并通过 @RestControllerAdvice 或 @ControllerAdvice 注解注册为全局组件‌13。
‌HttpMessageConverter‌
需实现 HttpMessageConverter 接口，并通过 WebMvcConfigurer 配置类注册到 Spring 容器中‌13。
5. ‌协作关系‌
两者在请求处理流程中协作：

控制器方法返回数据后，先由 ResponseBodyAdvice 的 beforeBodyWrite() 方法处理数据‌13。
处理后的数据再由 HttpMessageConverter 序列化为 HTTP 响应内容‌13。
‌总结‌

‌ResponseBodyAdvice‌ 侧重对响应数据的全局预处理，属于业务逻辑层扩展。
‌HttpMessageConverter‌ 侧重数据格式的转换，属于协议层实现。

