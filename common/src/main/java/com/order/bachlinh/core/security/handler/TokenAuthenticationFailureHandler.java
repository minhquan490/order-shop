package com.order.bachlinh.core.security.handler;

import com.order.bachlinh.core.util.JacksonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for handle failure when authenticate user fail.
 *
 * @author Hoang Minh Quan
 * */
public class TokenAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        int code = HttpStatus.UNAUTHORIZED.value();
        Map<String, Object> res = new HashMap<>();
        res.put("message", exception.getMessage());
        res.put("code", code);
        response.setStatus(code);
        response.getOutputStream().write(JacksonUtils.writeObjectAsBytes(res));
        response.flushBuffer();
    }
}
