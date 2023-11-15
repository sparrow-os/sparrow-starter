package com.sparrow.spring.starter.filter;

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.ClientInfoConstant;
import com.sparrow.protocol.enums.Platform;
import com.sparrow.spring.starter.SpringServletContainer;
import com.sparrow.utility.StringUtility;
import eu.bitwalker.useragentutils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ClientInformationFilter implements OrderedFilter {
    private static Logger logger = LoggerFactory.getLogger(ClientInformationFilter.class);

    public ClientInformationFilter(int order, SpringServletContainer springServletContainer) {
        this.springServletContainer = springServletContainer;
        this.order = order;
    }

    private SpringServletContainer springServletContainer;

    private int order;

    @Override
    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        ClientInformation clientInformation = new com.sparrow.protocol.ClientInformation();
        clientInformation.setIp(springServletContainer.getClientIp());
        logger.info("client ip {}", clientInformation.getIp());
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
        Browser browser = userAgent.getBrowser();
        logger.info("device type {},browser type {}", os.getDeviceType(), browser.getBrowserType());
        if (os.getDeviceType().equals(DeviceType.COMPUTER) || BrowserType.MOBILE_BROWSER.equals(browser.getBrowserType())) {
            clientInformation.setOs(os.getGroup().getName());
            clientInformation.setPlatform(Platform.PC);
            clientInformation.setDevice(browser.getName());
            clientInformation.setDeviceId(clientInformation.getIp());
        }

        String simulate = request.getHeader(ClientInfoConstant.SIMULATE);
        if (!StringUtility.isNullOrEmpty(simulate)) {
            clientInformation.setSimulate(Boolean.valueOf(simulate));
        }
        ThreadContext.bindClientInfo(clientInformation);
        chain.doFilter(request, response);
        ThreadContext.clearClient();
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
