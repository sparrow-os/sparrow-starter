package com.sparrow.spring.starter;

import com.sparrow.constant.Config;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.web.ResultAssembler;
import com.sparrow.utility.ConfigUtility;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Inject
    private ServletContainer servletContainer;

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Object handle(HttpServletRequest request, BusinessException e, RedirectAttributes attr) {
        String contentType = request.getContentType();
        if (contentType == null || Constant.CONTENT_TYPE_FORM.equals(contentType)) {
            String referer = servletContainer.referer();
            ModelAndViewUtils.failFlash(request, ResultAssembler.assemble(e, ConfigUtility.getValue(Config.LANGUAGE)));
            return new ModelAndView("redirect:/error?" + referer);
        }
        return Result.fail(e);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception exception) {
        logger.error("global exception ", exception);
        String contentType = request.getContentType();
        if (contentType == null || Constant.CONTENT_TYPE_FORM.equals(contentType)) {
            String referer = servletContainer.referer();
            ModelAndViewUtils.failFlash(request, Result.fail());
            return new ModelAndView("redirect:/error?" + referer);
        }
        return Result.fail();
    }
}