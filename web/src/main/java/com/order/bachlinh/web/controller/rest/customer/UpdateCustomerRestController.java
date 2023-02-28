package com.order.bachlinh.web.controller.rest.customer;

import com.order.bachlinh.web.component.dto.form.CustomerUpdateForm;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UpdateCustomerRestController {
    private static final String UPDATE_CUSTOMER = "/customer/update";

    private final CustomerService customerService;

    @PutMapping(path = UPDATE_CUSTOMER)
    public ResponseEntity<Map<String, String>> updateInformation(@RequestBody CustomerUpdateForm form) {
        boolean result = customerService.updateCustomer(form);
        Map<String, String> resp = new HashMap<>();
        if (result) {
            resp.put("message", "Update success");
            return ResponseEntity.ok(resp);
        } else {
            resp.put("message", "Update failure");
            return ResponseEntity.badRequest().body(resp);
        }
    }
}
