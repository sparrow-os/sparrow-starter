package com.sparrow.spring.starter;

import com.sparrow.constant.Config;
import com.sparrow.datasource.DatasourceConfigReader;
import com.sparrow.email.EmailSender;
import com.sparrow.image.ImageExtractorRegistry;
import com.sparrow.io.FileService;
import com.sparrow.io.impl.JDKFileService;
import com.sparrow.spring.starter.Interceptor.FlashParamPrepareAspect;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.support.AuthenticatorConfigReader;
import com.sparrow.support.IpSupport;
import com.sparrow.support.ip.SparrowIpSupport;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@Slf4j
public class SparrowAutoConfiguration {
    public SparrowAutoConfiguration() {
        log.info("Sparrow Auto Configuration INIT");
    }

    @Autowired
    private SparrowConfig sparrowConfig;

    @Bean
    public AuthenticatorConfigReader authenticatorConfigReader() {
        return sparrowConfig.getAuthenticator();
    }

    @Bean
    public WebConfigReader webConfigReader() {
        return this.sparrowConfig.getMvc();
    }

    @Bean
    public DatasourceConfigReader datasourceConfigReader() {
        return sparrowConfig.getDataSource();
    }


    @Bean
    @ConditionalOnMissingBean(CookieUtility.class)
    public CookieUtility cookieUtility() {
        return new CookieUtility();
    }

    @Bean
    @ConditionalOnMissingBean(SpringServletContainer.class)
    public SpringServletContainer springServletContainer() {
        return new SpringServletContainer();
    }

    @Bean
    @ConditionalOnMissingBean(FlashParamPrepareAspect.class)
    public FlashParamPrepareAspect flashParamPrepareAspect() {
        return new FlashParamPrepareAspect();
    }

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService fileService() {
        return new JDKFileService();
    }

    @Bean
    @ConditionalOnMissingBean(IpSupport.class)
    public IpSupport ipSupport() {
        return new SparrowIpSupport();
    }

    @Bean
    @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "sparrow.email", name = "enabled", havingValue = "true")
    public EmailSender emailSender() {
        SparrowConfig.Email email = sparrowConfig.getEmail();
        String emailPassword = System.getenv(Config.EMAIL_PASSWORD);
        if (email.getDebugPassword()) {
            log.info("online password {}", emailPassword);
        }
        if (StringUtility.isNullOrEmpty(emailPassword)) {
            emailPassword = email.getPassword();
        }
        log.info("final password {}", emailPassword);
        return new EmailSender(email.getLocalAddress(), email.getHost(), email.getFrom(), email.getUsername(), emailPassword);
    }

    @Bean
    @ConditionalOnMissingBean(ImageExtractorRegistry.class)
    public ImageExtractorRegistry imageExtractorRegistry() {
        return new ImageExtractorRegistry();
    }
}
