package com.sparrow.spring.starter.resolver;

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.ClientInfoConstant;
import com.sparrow.protocol.enums.Platform;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.StringUtility;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Named
public class ClientInfoArgumentResolvers implements HandlerMethodArgumentResolver {
    private static Logger logger = LoggerFactory.getLogger(ClientInfoArgumentResolvers.class);

    @Override public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().getParameterizedType().equals(ClientInformation.class);
    }

    @Override public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer
        container,
        NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        return ThreadContext.getClientInfo();
    }
}
