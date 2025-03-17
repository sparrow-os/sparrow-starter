package com.sparrow.spring.starter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "sparrow")
@Data
public class SparrowConfig {

    @Value("${profile:dev}")
    private String profile;
    /**
     * 默认不支持模板引擎
     */
    @Value("${exception.template.support:false}")
    private Boolean supportTemplate;

    @Value("${exception.api.prefix:}")
    private String apiPrefix;

    @Value("${allowed.origins:false}")
    private String allowedOrigins;

    @Value("${datasource.password.key:mysql_sparrow_password}")
    private String passwordKey;

    @Value("${authenticator.encrypt_key:}")
    private String encryptKey;

    @Value("${authenticator.validate_device_id:true}")
    private Boolean validateDeviceId;

    @Value("${authenticator.validate_status:true}")
    private Boolean validateStatus;

    @Value("${authenticator.exclude-patterns:}")
    private String excludePatterns;

    @Value("${authenticator.mock.login.user:false}")
    private Boolean mockUser;
}
