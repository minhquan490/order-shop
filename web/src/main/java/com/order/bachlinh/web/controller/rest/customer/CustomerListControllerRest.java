package com.order.bachlinh.web.controller.rest.customer;

import com.order.bachlinh.web.component.dto.resp.CustomerResp;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerListControllerRest {
    private static final String CUSTOMER_LIST = "/admin/customer/list";
    private static final int PAGE_SIZE = 100;

    private final CustomerService customerService;

    @GetMapping(path = CUSTOMER_LIST)
    public ResponseEntity<Page<CustomerResp>> customerList(@RequestParam(name = "page", required = false) String page) {
        PageRequest pageRequest;
        if (page == null) {
            pageRequest = PageRequest.of(1, PAGE_SIZE);
        } else {
            pageRequest = PageRequest.of(Integer.parseInt(page), PAGE_SIZE);
        }
        Page<CustomerResp> customers = customerService.getFullInformationOfCustomer(pageRequest);
        return ResponseEntity.ok(customers);
    }
}
