package com.stefanski.loan.rest.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefanski.loan.rest.error.RestExceptionHandler;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.stefanski.loan.util.TestHelper.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Dariusz Stefanski
 */
public class ControllerIntegrationTest {

    protected MockMvc mockMvc;

    protected void setup(Object controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setHandlerExceptionResolvers(createExceptionResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    protected MockHttpServletRequestBuilder postWithJson(String url, Object object) throws IOException {
        return post(url)
                .contentType(APPLICATION_JSON_UTF8)
                .content(object2JsonBytes(object));
    }

    private byte[] object2JsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    // http://stackoverflow.com/questions/15302243/spring-mvc-controllers-unit-test-not-calling-controlleradvice
    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new RestExceptionHandler(), method);
            }
        };
        // My extension
        List<HttpMessageConverter<?>> messageConverters = Arrays.asList(new MappingJackson2HttpMessageConverter());
        exceptionResolver.setMessageConverters(messageConverters);
        // end of my extension

        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }
}
