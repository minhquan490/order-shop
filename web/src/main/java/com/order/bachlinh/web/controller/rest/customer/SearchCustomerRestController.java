package com.order.bachlinh.web.controller.rest.customer;

import com.order.bachlinh.web.component.dto.resp.CustomerResp;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchCustomerRestController {
    private static final String SEARCH_CUSTOMER = "/admin/customer/{id}";

    private final CustomerService customerService;

    @GetMapping(path = SEARCH_CUSTOMER)
    public ResponseEntity<CustomerResp> searchCustomer(@PathVariable("id") String customerId) {
        CustomerResp resp = customerService.getFullInfortmation(customerId);
        return ResponseEntity.ok(resp);
    }
}
