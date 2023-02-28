package com.order.bachlinh.web.controller.rest.customer;

import com.order.bachlinh.web.component.dto.form.CreateCustomerForm;
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
public class CreateCustomerRestController {
    private static final String CREATE_CUSTOMER_URL = "/admin/customer/create";

    private final CustomerService customerService;

    @PostMapping(path = CREATE_CUSTOMER_URL)
    public ResponseEntity<Map<String, String>> createCustomer(@RequestBody CreateCustomerForm form) {
        boolean result = customerService.createCustomer(form);
        Map<String, String> resp = new HashMap<>();
        if (result) {
            resp.put("message", "Create success");
            return ResponseEntity.ok(resp);
        } else {
            resp.put("message", "Create failure");
            return ResponseEntity.badRequest().body(resp);
        }
    }
}
