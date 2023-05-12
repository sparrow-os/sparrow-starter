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

package com.sparrow.spring.starter.Interceptor;

import com.sparrow.protocol.POJO;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.spring.starter.ModelAndViewUtils;
import com.sparrow.spring.starter.SpringContext;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FlashParamPrepareAspect {

    private static final AtomicLong BASE_COUNT = new AtomicLong();

    @Pointcut("@within(org.springframework.web.bind.annotation.RequestMapping)")
    public void flashParamPointCut() {
    }

    @Around("flashParamPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //String methodName = point.getTarget().getClass().getName() + "." + signature.getName();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);

        ServletContainer servletContainer = SpringContext.getContext().getBean(ServletContainer.class);

        for (int i = 0; i < args.length; i++) {
            Object obj = args[i];
            if (obj instanceof POJO) {
                servletContainer.getRequest().setAttribute(ModelAndViewUtils.extractedPojoKey((POJO) obj), obj);
                continue;
            }
            if (paramNames != null) {
                servletContainer.getRequest().setAttribute(paramNames[i], obj);
            }
        }
        return point.proceed();
    }
}
