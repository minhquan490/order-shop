package com.order.bachlinh.web.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HttpRequestMethodControllerAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        Map<String, Object> resp = new HashMap<>(2);
        resp.put("message", e.getMessage());
        resp.put("code", HttpStatus.BAD_REQUEST.value());
        return resp;
    }
}
