/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.spring.filter;

import com.sparrow.spring.filter.monitor.Monitor;
import com.sparrow.support.web.ServletUtility;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

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
