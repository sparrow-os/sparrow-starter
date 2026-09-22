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
package com.sparrow.spring.mvc.message.converter;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.Result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

@Deprecated
public class ListJsonMessageConverter extends AbstractHttpMessageConverter<List<?>> {

    private Json json = JsonFactory.getProvider();

    public ListJsonMessageConverter() {
        super(new MediaType("application", "json", StandardCharsets.UTF_8));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        if (clazz.getTypeParameters().length == 0) {
            return false;
        }
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    protected List<?> readInternal(Class<? extends List<?>> aClass,
                                   HttpInputMessage message) throws HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void writeInternal(List<?> voList,
                              HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Result<List<?>> result = new Result<>(voList);
        outputMessage.getBody().write(this.json.toString(result).getBytes());
    }
}
