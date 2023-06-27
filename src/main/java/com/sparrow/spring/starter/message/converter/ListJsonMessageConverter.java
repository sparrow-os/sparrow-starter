package com.sparrow.spring.starter.message.converter;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.Result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.inject.Named;

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
