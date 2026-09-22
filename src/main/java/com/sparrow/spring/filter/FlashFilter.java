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

import com.sparrow.core.Pair;
import com.sparrow.lang.url.UrlMatcher;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.web.ServletUtility;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * spring 实现的flash 是url 变化后自动清除session
 * 本方案支持中间跳转状态保持功能
 * ModelAndViewUtils 为实现该功能提供工具类
 */
public class FlashFilter extends OncePerRequestFilter implements Filter {
    private static ServletUtility servletUtility = ServletUtility.getInstance();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String actionKey = servletUtility.getActionKey(request);
        Pair<String, Map<String, Object>> sessionPair = (Pair<String, Map<String, Object>>) request.getSession().getAttribute(Constant.FLASH_KEY);
        if (sessionPair == null) {
            chain.doFilter(request, response);
            return;
        }

        if (new UrlMatcher(sessionPair.getFirst(), actionKey).match(request)) {
            Map<String, Object> values = sessionPair.getSecond();
            for (String key : values.keySet()) {
                request.setAttribute(key, values.get(key));
            }
            chain.doFilter(request, response);
            return;
        }
        //url换掉时，则session 被清空 （非include）
        request.getSession().removeAttribute(Constant.FLASH_KEY);
        chain.doFilter(request, response);
    }

    /**
     * @param request
     * @return
     * @throws ServletException
     * @see https://github.com/spring-projects/spring-boot/issues/7426
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String actionKey = servletUtility.getActionKey(request);
        return actionKey.endsWith("favicon.ico");
    }
}
