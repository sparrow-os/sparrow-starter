package com.sparrow.spring.starter.filter;

import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.support.AttributeContext;
import com.sparrow.support.web.AbstractGlobalAttributeFilter;

public class SpringGlobalAttributeFilter extends AbstractGlobalAttributeFilter {

    private SparrowConfig sparrowConfig;

    public SpringGlobalAttributeFilter(SparrowConfig sparrowConfig) {
        this.sparrowConfig = sparrowConfig;
    }

    @Override
    public AttributeContext parseAttributeContext() {
        SparrowConfig sparrowConfig = this.sparrowConfig;
        return new AttributeContext() {
            @Override
            public String getRootPath() {
                return sparrowConfig.getMvc().getRootPath();
            }

            @Override
            public String getLanguage() {
                return sparrowConfig.getMvc().getLanguage();
            }

            @Override
            public String getResource() {
                return sparrowConfig.getMvc().getResource();
            }

            @Override
            public String getResourceVersion() {
                return sparrowConfig.getMvc().getResourceVersion();
            }

            @Override
            public String getUpload() {
                return sparrowConfig.getMvc().getUpload();
            }

            @Override
            public String getInternationalization() {
                return sparrowConfig.getMvc().getInternationalization();
            }
        };
    }
}
