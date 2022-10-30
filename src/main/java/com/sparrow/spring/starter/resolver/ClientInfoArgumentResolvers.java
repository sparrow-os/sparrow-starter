package com.sparrow.spring.starter.resolver;

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.ThreadContext;
import javax.inject.Named;
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
