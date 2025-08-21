package com.sparrow.spring.starter.filter;

import com.sparrow.spring.starter.monitor.Monitor;
import com.sparrow.support.web.ServletUtility;
import org.springframework.boot.web.servlet.filter.OrderedFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AccessMonitorFilter implements Filter {
    private static final String FAV_ICON = "/favicon.ico";
    private Monitor monitor;

    public AccessMonitorFilter(Monitor monitor) {
        this.monitor = monitor;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String currentServletPath = httpServletRequest.getServletPath();
        if (FAV_ICON.equals(currentServletPath)) {
            chain.doFilter(request, response);
            return;
        }
        String ip = ServletUtility.getInstance().getClientIp(request);
        this.monitor.access(ip);
        chain.doFilter(request, response);
    }
}
