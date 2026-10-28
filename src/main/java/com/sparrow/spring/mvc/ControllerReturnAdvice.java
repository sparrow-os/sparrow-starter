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
package com.sparrow.spring.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.spring.config.SparrowConfig;
import com.sparrow.support.web.ServletUtility;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 必须要在应用层加
 * @ControllerAdvice 开户全局返回值处理
 */
@Slf4j
public class ControllerReturnAdvice implements ResponseBodyAdvice<Object> {
    public ControllerReturnAdvice() {
        log.info("ControllerReturnAdvice INIT");
    }

    @Inject
    private ServletContainer servletContainer;

    @Autowired
    private SparrowConfig sparrowConfig;


    private boolean isAjax(HttpServletRequest request) {
        SparrowConfig.Mvc templateEngine = this.sparrowConfig.getMvc();
        return ServletUtility.getInstance().isAjax(request, templateEngine.getSupportTemplateEngine(), templateEngine.getAjaxPattens());
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Object handle(HttpServletRequest request, BusinessException e, RedirectAttributes attr) {
        log.info("business request rejected: {}", e.getErrorSupport().getCode());
        if (this.isAjax(request)) {
            return ResponseEntity.badRequest().body(Result.fail(e));
        }
        String referer = servletContainer.referer();
        String rootPath = this.sparrowConfig.getMvc().getRootPath();
        ModelAndViewUtils.failFlash(request, Result.fail(e));
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception exception) {
        HttpStatusCode status = frameworkStatus(exception);
        boolean clientError = status.is4xxClientError();
        if (clientError) {
            log.warn("invalid HTTP request: {}", exception.getClass().getName());
        } else {
            // 异常 message 可能含请求正文、令牌或供应商凭据；只记录类型与栈帧。
            log.error("global exception type={} stack={}", exception.getClass().getName(), safeStack(exception));
        }
        Result<?> result = clientError
                ? Result.fail(SparrowError.SYSTEM_ILLEGAL_REQUEST)
                : Result.fail(SparrowError.SYSTEM_SERVER_ERROR);
        if (this.isAjax(request)) {
            return ResponseEntity.status(status).body(result);
        }
        String referer = servletContainer.referer();
        ModelAndViewUtils.failFlash(request, result);
        String rootPath = this.sparrowConfig.getMvc().getRootPath();
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }

    private HttpStatusCode frameworkStatus(Exception exception) {
        if (exception instanceof ErrorResponse errorResponse) {
            return errorResponse.getStatusCode();
        }
        if (exception instanceof HttpMessageNotReadableException || exception instanceof TypeMismatchException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String safeStack(Throwable failure) {
        StringBuilder trace = new StringBuilder();
        var seen = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<Throwable, Boolean>());
        int depth = 0;
        for (Throwable current = failure; current != null && depth < 8 && seen.add(current); current = current.getCause(), depth++) {
            trace.append(current.getClass().getName()).append('\n');
            StackTraceElement[] frames = current.getStackTrace();
            for (int i = 0; i < Math.min(frames.length, 24); i++) {
                trace.append("\tat ").append(frames[i]).append('\n');
            }
        }
        return trace.toString();
    }


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // response是Result类型，或者注释了NotControllerResponseAdvice都不进行包装
        //如果不是Result
        boolean isResult = Result.class.isAssignableFrom(methodParameter.getParameterType());
        //不是ModelAndView
        boolean isModelAndView = methodParameter.getParameterType().equals(ModelAndView.class);
        //才会被封装
        return !isResult && !isModelAndView;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest
            request, ServerHttpResponse response) {
        // ResponseEntity<Result<?>> 的声明类型不是 Result，实际正文仍不应再次包装。
        if (data instanceof Result<?>) {
            return data;
        }
        // 下载场景(响应头设置了 attachment)不进行包装
        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        if (contentDisposition != null && contentDisposition.toLowerCase().contains("attachment")) {
            return data;
        }
        // String类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在Result里后转换为json串进行返回
                return objectMapper.writeValueAsString(new Result(data));
            } catch (JsonProcessingException e) {
                return Result.fail();
            }
        }
        return new Result(data);
    }
}
