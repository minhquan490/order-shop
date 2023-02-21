package com.order.bachlinh.web.controller.rest;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.security.handler.ClientSecretHandler;
import com.order.bachlinh.core.util.HeaderUtils;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LogoutControllerRest {
    private static final String LOGOUT_PATH = "/logout";
    private static final String CLIENT_COOKIE_KEY = "client-secret";
    private final ClientSecretHandler clientSecretHandler;
    private final CustomerService customerService;

    @GetMapping(path = LOGOUT_PATH)
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        boolean result = customerService.logout((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Map<String, Object> resp = new HashMap<>(2);
        if (result) {
            clientSecretHandler.removeClientSecret(null, HeaderUtils.getRequestHeaderValue(CLIENT_COOKIE_KEY, request));
            resp.put("message", "Logout success");
            resp.put("code", HttpStatus.OK.value());
            return ResponseEntity.ok(resp);
        } else {
            resp.put("message", "Logout failure, please contact for admin");
            resp.put("code", HttpStatus.SERVICE_UNAVAILABLE);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(resp);
        }
    }
}
