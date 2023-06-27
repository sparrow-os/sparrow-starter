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
```**

