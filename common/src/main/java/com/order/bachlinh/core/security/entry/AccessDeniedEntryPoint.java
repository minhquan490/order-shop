package com.order.bachlinh.core.security.entry;

import com.order.bachlinh.core.util.JacksonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccessDeniedEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        int code = HttpStatus.FORBIDDEN.value();
        Map<String, Object> res = new HashMap<>();
        res.put("message", accessDeniedException.getMessage());
        res.put("code", code);
        response.setStatus(code);
        response.getOutputStream().write(JacksonUtils.writeObjectAsBytes(res));
        response.flushBuffer();
    }
}
