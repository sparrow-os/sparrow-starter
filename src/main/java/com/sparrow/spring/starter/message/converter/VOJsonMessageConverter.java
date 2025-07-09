package com.sparrow.spring.starter.message.converter;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.DTO;
import com.sparrow.protocol.Result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

@Deprecated
public class VOJsonMessageConverter extends AbstractHttpMessageConverter<DTO> {
    private Json json = JsonFactory.getProvider();

    public VOJsonMessageConverter() {
        super(new MediaType("application", "json", StandardCharsets.UTF_8));
    }

    @Override
    public boolean supports(Class clazz) {
        return ViewObjectUtils.isViewObject(clazz);
    }

    @Override
    protected DTO readInternal(Class<? extends DTO> clazz,
                               HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(DTO result,
                                 HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (result instanceof Result) {
            outputMessage.getBody().write(this.json.toString(result).getBytes());
            return;
        }
        Result<DTO> voResult = new Result<DTO>(result);
        outputMessage.getBody().write(this.json.toString(voResult).getBytes());
    }
}
