package com.sparrow.spring.starter.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "sparrow")
@Data
@ToString
/**
 * 学习文档
 * https://sparrowzoo.feishu.cn/docx/HatsdGWMeoi036x28vQcsKeNnOb
 */
public class SparrowConfig {
    private String profile = "dev";
    private String allowedOrigins = "*";
    private Exception exception;
    private Authenticator authenticator;


    @Data
    @ToString
    public static class Exception {
        /**
         * 默认不支持模板引擎
         */
        private Boolean supportTemplate = false;

        private String apiPrefix;

    }

    @Data
    @ToString
    public static class Authenticator {
        private String encryptKey;

        private Boolean validateDeviceId = true;

        private Boolean validateStatus = true;

        private String excludePatterns;

        private Boolean mockLoginUser = false;

        private String datasourcePasswordKey;
    }
}
