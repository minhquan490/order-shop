package com.order.bachlinh.web.controller.rest;

import com.order.bachlinh.core.util.HeaderUtils;
import com.order.bachlinh.web.component.dto.form.LoginForm;
import com.order.bachlinh.web.component.dto.resp.LoginResp;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginRestController {
    private static final String LOGIN_URL = "/login";

    private final CustomerService customerService;

    @PostMapping(path = LOGIN_URL)
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {
        LoginResp loginResp = customerService.login(loginForm);
        Map<String, Object> result = new HashMap<>();
        if (!loginResp.isLogged()) {
            result.put("message", "Login failure");
            result.put("code", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        } else {
            result.put("refreshToken", loginResp.refreshToken());
            HeaderUtils.setAuthorizeHeader(loginResp.accessToken(), response);
            return ResponseEntity.ok(result);
        }
    }
}
