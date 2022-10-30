package com.sparrow.spring.starter.filter;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.LoginToken;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.Constant;
import java.io.IOException;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Named
public class LoginTokenFilter implements Filter {
    private Json json = JsonFactory.getProvider();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String loginTokenOfHeader = req.getHeader(Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN);
            LoginToken loginToken = this.json.parse(loginTokenOfHeader, LoginToken.class);
            ThreadContext.bindLoginToken(loginToken);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}