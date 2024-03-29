package com.sparrow.spring.starter.filter;

import com.sparrow.spring.starter.SpringServletContainer;
import com.sparrow.spring.starter.monitor.Monitor;
import org.springframework.boot.web.servlet.filter.OrderedFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AccessMonitorFilter implements OrderedFilter {
    private static final String FAV_ICON = "/favicon.ico";
    private Monitor monitor;
    private Integer order;
    private SpringServletContainer springServletContainer;


    public AccessMonitorFilter(Monitor monitor, Integer order,SpringServletContainer springServletContainer) {
        this.springServletContainer=springServletContainer;
        this.monitor = monitor;
        this.order = order;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String currentServletPath = httpServletRequest.getServletPath();
        if (FAV_ICON.equals(currentServletPath)) {
            chain.doFilter(request, response);
            return;
        }
        String ip = this.springServletContainer.getClientIp();
        this.monitor.access(ip);
        chain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
