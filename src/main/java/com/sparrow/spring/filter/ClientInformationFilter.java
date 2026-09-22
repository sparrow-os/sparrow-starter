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

import com.sparrow.context.SessionContext;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.constant.ClientInfoConstant;
import com.sparrow.protocol.enums.Platform;
import com.sparrow.spring.container.SpringServletContainer;
import com.sparrow.utility.StringUtility;
import eu.bitwalker.useragentutils.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class ClientInformationFilter implements Filter {
    public ClientInformationFilter(SpringServletContainer springServletContainer) {
        this.springServletContainer = springServletContainer;
    }

    private SpringServletContainer springServletContainer;

    @Override
    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        ClientInformation clientInformation = new com.sparrow.protocol.ClientInformation();
        clientInformation.setIp(springServletContainer.getClientIp());
        log.info("client ip {}", clientInformation.getIp());
        String appId = request.getHeader(ClientInfoConstant.APP_ID);
        if (!StringUtility.isNullOrEmpty(appId)) {
            clientInformation.setAppId(Integer.parseInt(appId));
        }

        String appVersion = request.getHeader(ClientInfoConstant.APP_VERSION);
        if (!StringUtility.isNullOrEmpty(appVersion)) {
            clientInformation.setAppVersion(Float.parseFloat(appVersion));
        }

        clientInformation.setBssid(request.getHeader(ClientInfoConstant.BSSID));
        clientInformation.setChannel(request.getHeader(ClientInfoConstant.CHANNEL));
        clientInformation.setClientVersion(request.getHeader(ClientInfoConstant.CLIENT_VERSION));

        if (request.getHeader(ClientInfoConstant.DEVICE_TYPE) != null) {
            clientInformation.setDeviceType(com.sparrow.protocol.enums.DeviceType.valueOf(request.getHeader(ClientInfoConstant.DEVICE_TYPE)));
        }
        clientInformation.setDevice(request.getHeader(ClientInfoConstant.DEVICE));
        clientInformation.setDeviceId(request.getHeader(ClientInfoConstant.DEVICE_ID));
        clientInformation.setDeviceModel(request.getHeader(ClientInfoConstant.DEVICE_MODEL));

        clientInformation.setIdfa(request.getHeader(ClientInfoConstant.IDFA));
        clientInformation.setImei(request.getHeader(ClientInfoConstant.IMEI));
        String latitude = request.getHeader(ClientInfoConstant.LATITUDE);
        if (!StringUtility.isNullOrEmpty(latitude)) {
            clientInformation.setLatitude(Double.parseDouble(request.getHeader(ClientInfoConstant.LATITUDE)));
        }

        String longitude = request.getHeader(ClientInfoConstant.LONGITUDE);
        if (!StringUtility.isNullOrEmpty(longitude)) {
            clientInformation.setLongitude(Double.parseDouble(request.getHeader(ClientInfoConstant.LONGITUDE)));
        }

        clientInformation.setOs(request.getHeader(ClientInfoConstant.OS));
        clientInformation.setNetwork(request.getHeader(ClientInfoConstant.NETWORK));
        String startTime = request.getHeader(ClientInfoConstant.START_TIME);
        if (!StringUtility.isNullOrEmpty(startTime)) {
            clientInformation.setStartTime(Long.parseLong(startTime));
        }
        String resumeTime = request.getHeader(ClientInfoConstant.RESUME_TIME);

        if (!StringUtility.isNullOrEmpty(resumeTime)) {
            clientInformation.setResumeTime(Long.parseLong(resumeTime));
        }
        //clientInformation.setWebsite(rootPath);
        clientInformation.setUserAgent(request.getHeader(ClientInfoConstant.USER_AGENT));
        UserAgent userAgent = UserAgent.parseUserAgentString(clientInformation.getUserAgent());
        OperatingSystem os = userAgent.getOperatingSystem();
        clientInformation.setOs(os.getGroup().getName());
        Browser browser = userAgent.getBrowser();
        clientInformation.setDevice(browser.getName());
        clientInformation.setDeviceId(clientInformation.getIp());
        log.info("device type {},browser type {}", os.getDeviceType(), browser.getBrowserType());
        if (os.getDeviceType().equals(DeviceType.COMPUTER)) {
            clientInformation.setPlatform(Platform.PC);
            clientInformation.setDeviceType(com.sparrow.protocol.enums.DeviceType.PC);
        }
        if (browser.getBrowserType().equals(BrowserType.MOBILE_BROWSER)) {
            clientInformation.setDeviceType(com.sparrow.protocol.enums.DeviceType.MOBILE);
        }

        String simulate = request.getHeader(ClientInfoConstant.SIMULATE);
        if (!StringUtility.isNullOrEmpty(simulate)) {
            clientInformation.setSimulate(Boolean.valueOf(simulate));
        }
        SessionContext.bindClientInfo(clientInformation);
        chain.doFilter(request, response);
        SessionContext.clearClient();
    }
}
