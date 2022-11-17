package com.sparrow.spring.starter.message.converter;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.VO;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
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
        if (clazz == byte.class || clazz == Byte.class) {
            return true;
        }

        if (clazz == char.class || clazz == Character.class) {
            return true;
        }

        if (clazz == short.class || clazz == Short.class) {
            return true;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return true;
        }

        if (clazz == long.class || clazz == Long.class) {
            return true;
        }

        if (clazz == float.class || clazz == Float.class) {
            return true;
        }

        if (clazz == boolean.class || clazz == Boolean.class) {
            return true;
        }

        if (clazz == double.class || clazz == Double.class) {
            return true;
        }

        if (clazz == Date.class) {
            return true;
        }

        if (clazz == Timestamp.class) {
            return true;
        }

        if (clazz == BigDecimal.class) {
            return true;
        }
        return false;
    }

    @Override
    protected void writeInternal(Object result,
        HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Result<Object> voResult = new Result<>(result);
        outputMessage.getBody().write(this.json.toString(voResult).getBytes());
    }
}
