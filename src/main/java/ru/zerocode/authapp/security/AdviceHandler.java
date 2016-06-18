package ru.zerocode.authapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ru.zerocode.authapp.model.protocol.RestError;
import ru.zerocode.authapp.model.protocol.RestResponse;

import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 * Адвайс хенддлер, для хендлинга Ошибок, добавление всегда RestError объекта
 * в т.ч. добавление RestResponse на все типы ответов
 * */
@ControllerAdvice
public class AdviceHandler implements ResponseBodyAdvice<Object> {
    static final Logger logger = LoggerFactory.getLogger(AdviceHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResponse genericExceptionHandler(Exception e) {
        logger.warn(e.getMessage(),e);
        RestResponse res = new RestResponse(false);
        res.addError(new RestError(400,e.getMessage()));
        return res;
    }

    @ExceptionHandler(LoginException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResponse loginException(Exception e) {
        logger.warn(e.getMessage(),e);
        RestResponse res = new RestResponse(false);
        res.addError(new RestError(1,e.getMessage()));
        return res;
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResponse ioException(Exception e) {
        logger.warn(e.getMessage(),e);
        RestResponse res = new RestResponse(false);
        res.addError(new RestError(3,e.getMessage()));
        return res;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * Непосредственно добавление (wrapper) ответа в RestResponse
     * */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return o instanceof RestResponse ? o : new RestResponse(o);
    }
}
