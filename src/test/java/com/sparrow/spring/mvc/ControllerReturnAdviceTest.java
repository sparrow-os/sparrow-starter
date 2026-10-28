package com.sparrow.spring.mvc;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.spring.config.SparrowConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ControllerReturnAdviceTest {
    private ControllerReturnAdvice advice;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        advice = new ControllerReturnAdvice();
        SparrowConfig config = new SparrowConfig();
        config.setMvc(new SparrowConfig.Mvc());
        ReflectionTestUtils.setField(advice, "sparrowConfig", config);
        request = new MockHttpServletRequest();
    }

    @Test
    void expectedBusinessRejectionUses400AndKeepsBusinessCode() {
        BusinessException failure = new BusinessException(SparrowError.GLOBAL_PARAMETER_IS_ILLEGAL);

        ResponseEntity<?> response = (ResponseEntity<?>) advice.handle(request, failure, null);

        assertEquals(400, response.getStatusCode().value());
        Result<?> body = (Result<?>) response.getBody();
        assertEquals(failure.getErrorSupport().getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }

    @Test
    void mvcRequestFailuresKeepTheirHttpStatus() {
        HttpInputMessage input = new MockHttpInputMessage(new byte[0]);
        ResponseEntity<?> malformed = (ResponseEntity<?>) advice.exceptionHandler(request,
                new HttpMessageNotReadableException("invalid JSON", input));
        ResponseEntity<?> mismatch = (ResponseEntity<?>) advice.exceptionHandler(request,
                new TypeMismatchException("x", Integer.class));
        ResponseEntity<?> wrongMethod = (ResponseEntity<?>) advice.exceptionHandler(request,
                new HttpRequestMethodNotSupportedException("PUT"));

        assertEquals(400, malformed.getStatusCode().value());
        assertEquals(400, mismatch.getStatusCode().value());
        assertEquals(405, wrongMethod.getStatusCode().value());
        assertFalse(((Result<?>) malformed.getBody()).isSuccess());
    }

    @Test
    void unexpectedExceptionUses500WithoutExposingItsMessage() {
        ResponseEntity<?> response = (ResponseEntity<?>) advice.exceptionHandler(request,
                new IllegalStateException("private database detail"));

        assertEquals(500, response.getStatusCode().value());
        Result<?> body = (Result<?>) response.getBody();
        assertEquals(SparrowError.SYSTEM_SERVER_ERROR.getCode(), body.getCode());
        assertFalse(body.getMessage().contains("private database detail"));
        String stack = (String) ReflectionTestUtils.invokeMethod(advice, "safeStack",
                new IllegalStateException("private database detail", new IllegalArgumentException("private token")));
        assertFalse(stack.contains("private database detail"));
        assertFalse(stack.contains("private token"));
    }

    @Test
    void mvcAppliesStatusesAndWritesOneResultEnvelope() throws Exception {
        TestAdvice mvcAdvice = new TestAdvice();
        ReflectionTestUtils.setField(mvcAdvice, "sparrowConfig", config());
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(mvcAdvice).build();

        mvc.perform(get("/business"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SparrowError.GLOBAL_PARAMETER_IS_ILLEGAL.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist());
        mvc.perform(post("/json").contentType("application/json").content("{bad-json"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(SparrowError.SYSTEM_SERVER_ERROR.getCode()));
        mvc.perform(get("/success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.data").value("done"));
    }

    private SparrowConfig config() {
        SparrowConfig config = new SparrowConfig();
        config.setMvc(new SparrowConfig.Mvc());
        return config;
    }

    @RestControllerAdvice
    static class TestAdvice extends ControllerReturnAdvice {
    }

    @RestController
    static class TestController {
        @GetMapping("/business")
        public String business() throws BusinessException {
            throw new BusinessException(SparrowError.GLOBAL_PARAMETER_IS_ILLEGAL);
        }

        @PostMapping("/json")
        public String json(@RequestBody Input input) {
            return input.name();
        }

        @GetMapping("/unexpected")
        public String unexpected() {
            throw new IllegalStateException("private database detail");
        }

        @GetMapping("/success")
        public ResponseEntity<Result<String>> success() {
            return ResponseEntity.ok(new Result<>("done"));
        }
    }

    record Input(String name) {
    }
}
