package com.sparrow.spring.starter.message.converter;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.Result;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.inject.Named;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

@Named
public class BasicTypeMessageConverter extends AbstractHttpMessageConverter<Object> {
    private Json json = JsonFactory.getProvider();

    public BasicTypeMessageConverter() {
        super(new MediaType("application", "json", StandardCharsets.UTF_8));
    }

    @Override protected Object readInternal(Class<?> aClass,
        HttpInputMessage message) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public boolean supports(Class clazz) {
        return ViewObjectUtils.isBasicType(clazz);
    }

    @Override
    protected void writeInternal(Object result,
        HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Result<Object> voResult = new Result<>(result);
        outputMessage.getBody().write(this.json.toString(voResult).getBytes());
    }
}
