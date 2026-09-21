package com.sparrow.spring.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.spring.config.SparrowConfig;
import com.sparrow.support.web.ServletUtility;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
        log.error("global exception ", e);
        if (this.isAjax(request)) {
            return Result.fail(e);
        }
        String referer = servletContainer.referer();
        String rootPath = this.sparrowConfig.getMvc().getRootPath();
        ModelAndViewUtils.failFlash(request, Result.fail(e));
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception exception) {
        log.error("global exception ", exception);
        Result result = Result.fail();
        if (this.isAjax(request)) {
            return result;
        }
        String referer = servletContainer.referer();
        ModelAndViewUtils.failFlash(request, result);
        String rootPath = this.sparrowConfig.getMvc().getRootPath();
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }


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
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest
            request, ServerHttpResponse response) {
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
