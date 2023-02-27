package com.order.bachlinh.web.controller.rest;

import com.order.bachlinh.web.component.dto.form.RegisterForm;
import com.order.bachlinh.web.component.dto.resp.RegisterResp;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RegisterControllerRest {
    private static final String REGISTER_URL = "/register";

    private final CustomerService customerService;

    @PostMapping(path = REGISTER_URL)
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterForm registerForm) {
        RegisterResp registerResp = customerService.register(registerForm);
        Map<String, Object> result = new HashMap<>();
        if (registerResp.isError()) {
            result.put("Message", registerResp.message());
            return ResponseEntity.badRequest().body(result);
        } else {
            result.put("Message", registerResp.message());
            return ResponseEntity.ok(result);
        }
    }
}
