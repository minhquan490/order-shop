package com.order.bachlinh.web.controller.advice;

import com.order.bachlinh.core.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidateControllerAdvice {

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidateError(ConstraintViolationException e) {
        Map<String, Object> resp = new HashMap<>(2);
        resp.put("message", e.getErrors());
        resp.put("code", HttpStatus.BAD_REQUEST.value());
        return resp;
    }
}
