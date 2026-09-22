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

import com.sparrow.spring.config.SparrowConfig;
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
