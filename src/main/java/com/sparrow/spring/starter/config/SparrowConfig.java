package com.sparrow.spring.starter.config;

import com.sparrow.datasource.DatasourceConfigReader;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.RegexUtility;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "sparrow")
@Data
@DependsOn("springContext")
@ToString
/**
 * 学习文档
 * https://sparrowzoo.feishu.cn/docx/HatsdGWMeoi036x28vQcsKeNnOb
 */
@Slf4j
public class SparrowConfig {
    public SparrowConfig() {
        log.info("SparrowConfig init");
    }

    private String profile = "dev";
    private Cors cors;
    private Mvc mvc;
    private Email email;
    private DataSource dataSource;
    private Enums enums;

    @Data
    @ToString
    public static class Enums {
        private List<String> coder;

        private List<String> business;
    }


    @Data
    @ToString
    public static class Mvc implements WebConfigReader {
        private List<String> autoMappingViewNames;
        private Boolean supportTemplateEngine = false;
        private List<String> ajaxPattens;
        private String rootPath;
        private String passport;
        private String language;
        private String resource;
        private String resourceVersion;
        private String upload;
        private String internationalization;
        private String defaultAvatar;
        private String loginUrl;
        private String imageExtension;
        //为兼容spring flash功能实现
        private String templateEngineSuffix;
        private String templateEnginePrefix;
        private String adminPage;
        private String errorPage;
        private String defaultWelcomePage;
        private String physicalUpload;
        private String physicalResource;
        private String waterMark;
        private String download;
        private String physicalDownload;

        public void setAjaxPattens(List<String> ajaxPattens) {
            this.ajaxPattens = RegexUtility.adapterWildcard(ajaxPattens);
        }
    }

    @Data
    @ToString
    public static class Email {
        private Boolean enabled = false;
        private String host;
        private String from;
        private String username;
        private String password;
        private String localAddress;
        private Boolean debugPassword = false;
    }

    @Data
    @ToString
    public static class Cors {
        private boolean allow = false;
        private List<String> allowedOrigins = new ArrayList<>();

    }

    @Data
    @ToString
    public static class DataSource implements DatasourceConfigReader {
        public DataSource() {
            log.info("data source config init");
        }

        private String defaultSchema;
        private String passwordKey;
        private Boolean debugDatasourcePassword;
    }
}
