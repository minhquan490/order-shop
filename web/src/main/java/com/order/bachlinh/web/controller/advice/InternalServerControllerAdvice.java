package com.order.bachlinh.web.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class InternalServerControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, Object> handleInternalError(Exception e) {
        log.error(e.getMessage(), e);
        Map<String, Object> result = new HashMap<>(2);
        result.put("message", "Service Unavailable");
        result.put("code", HttpStatus.SERVICE_UNAVAILABLE.value());
        return result;
    }
}
