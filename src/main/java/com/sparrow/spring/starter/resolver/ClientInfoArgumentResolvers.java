package com.sparrow.spring.starter.resolver;

import com.sparrow.context.SessionContext;
import com.sparrow.protocol.ClientInformation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class ClientInfoArgumentResolvers implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().getParameterizedType().equals(ClientInformation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer
            container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        return SessionContext.getClientInfo();
    }
}
