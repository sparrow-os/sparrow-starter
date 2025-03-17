package com.sparrow.spring.starter;

import com.sparrow.constant.Config;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private SparrowConfig sparrowConfig;


    private boolean isAjax(HttpServletRequest request) {
        return ServletUtility.getInstance().isAjax(request, this.sparrowConfig.getSupportTemplate(), this.sparrowConfig.getApiPrefix());
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Object handle(HttpServletRequest request, BusinessException e, RedirectAttributes attr) {
        logger.error("global exception ", e);
        if (this.isAjax(request)) {
            return Result.fail(e);
        }
        String referer = servletContainer.referer();
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        ModelAndViewUtils.failFlash(request, Result.fail(e));
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception exception) {
        logger.error("global exception ", exception);
        Result result = Result.fail();
        if (this.isAjax(request)) {
            return result;
        }
        String referer = servletContainer.referer();
        ModelAndViewUtils.failFlash(request, result);
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        return new ModelAndView("redirect:" + rootPath + "/error?" + referer);
    }
}
