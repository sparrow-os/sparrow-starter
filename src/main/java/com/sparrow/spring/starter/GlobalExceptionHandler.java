package com.sparrow.spring.starter;

import com.sparrow.constant.Config;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.web.ResultAssembler;
import com.sparrow.utility.ConfigUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Inject
    private ServletContainer servletContainer;

    private boolean isAjax(HttpServletRequest request) {
        String ajax = request.getHeader(Constant.IS_AJAX);
        if (ajax == null) {
            return false;
        }
        return ajax.equalsIgnoreCase("true");
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Object handle(HttpServletRequest request, BusinessException e, RedirectAttributes attr) {
        logger.error("global exception ", e);
        if (this.isAjax(request)) {
            Result result = Result.fail(e);
            result = ResultAssembler.assemble(result, ConfigUtility.getValue(Config.LANGUAGE));
            return result;
        }
        String referer = servletContainer.referer();
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        ModelAndViewUtils.failFlash(request, ResultAssembler.assemble(e, ConfigUtility.getValue(Config.LANGUAGE)));
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception exception) {
        logger.error("global exception ", exception);
        Result result = Result.fail();
        result = ResultAssembler.assemble(result, ConfigUtility.getValue(Config.LANGUAGE));

        if (this.isAjax(request)) {
            return result;
        }
        String referer = servletContainer.referer();
        ModelAndViewUtils.failFlash(request, result);
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }
}
