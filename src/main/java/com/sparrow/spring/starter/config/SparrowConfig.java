package com.sparrow.spring.starter.config;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.RegexUtility;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "sparrow")
@Data
@ToString
/**
 * 学习文档
 * https://sparrowzoo.feishu.cn/docx/HatsdGWMeoi036x28vQcsKeNnOb
 */
public class SparrowConfig {
    public SparrowConfig() {
        System.out.println("SparrowConfig init");
    }

    private String profile = "dev";
    private String allowedOrigins = "*";
    private Authenticator authenticator;
    private Mvc mvc;


    @Data
    @ToString
    public static class Authenticator {
        private String tokenKey = Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN;
        private String encryptKey;

        private Boolean validateDeviceId = true;

        private Boolean validateStatus = true;

        private List<String> excludePatterns;

        public void setExcludePatterns(List<String> excludePatterns) {
            this.excludePatterns = RegexUtility.adapterWildcard(excludePatterns);
        }

        private Boolean mockLoginUser = false;

        private String datasourcePasswordKey;
    }

    @Data
    @ToString
    public static class Mvc {
        private List<String> autoMappingViewNames;
        private Boolean supportTemplateEngine = false;
        private List<String> ajaxPattens;

        public void setAjaxPattens(List<String> ajaxPattens) {
            this.ajaxPattens = RegexUtility.adapterWildcard(ajaxPattens);
        }
    }
}
