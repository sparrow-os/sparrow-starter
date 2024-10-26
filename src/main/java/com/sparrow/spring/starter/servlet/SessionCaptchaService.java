package com.sparrow.spring.starter.servlet;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.CaptchaService;

public class SessionCaptchaService implements CaptchaService {
    private ServletContainer servletContainer;

    public SessionCaptchaService(ServletContainer servletContainer) {
        this.servletContainer = servletContainer;
    }

    @Override
    public String getCaptcha(String s) {
        return servletContainer.flash(Constant.VALIDATE_CODE);

    }

    @Override
    public void setCaptcha(String sessionId, String captcha) {
        servletContainer.flash(Constant.VALIDATE_CODE, captcha);
    }
}
