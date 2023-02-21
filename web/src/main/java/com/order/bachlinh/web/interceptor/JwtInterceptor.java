package com.order.bachlinh.web.interceptor;

import com.order.bachlinh.core.util.HeaderUtils;
import com.order.bachlinh.core.util.JacksonUtils;
import com.order.bachlinh.web.services.spi.business.InvalidateJwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final InvalidateJwtService invalidateJwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = HeaderUtils.getAuthorizeHeader(request);
        if (invalidateJwtService.isInvalidate(jwt)) {
            int code = HttpStatus.UNAUTHORIZED.value();
            Map<String, Object> res = new HashMap<>(2);
            res.put("message", "Access failure, login is required");
            res.put("code", code);
            response.setStatus(code);
            response.getOutputStream().write(JacksonUtils.writeObjectAsBytes(res));
            return false;
        } else {
            return true;
        }
    }
}
