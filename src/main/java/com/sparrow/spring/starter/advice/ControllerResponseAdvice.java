package com.sparrow.spring.starter.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparrow.protocol.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 功能描述:AOP拦截所有Controller
 */
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // response是Result类型，或者注释了NotControllerResponseAdvice都不进行包装
        //如果不是Result
        boolean isResult = methodParameter.getParameterType().isAssignableFrom(Result.class);
        //不是ModelAndView
        boolean isModelAndView = methodParameter.getParameterType().equals(ModelAndView.class);
        //才会被封装
        return !isResult && !isModelAndView;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
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
