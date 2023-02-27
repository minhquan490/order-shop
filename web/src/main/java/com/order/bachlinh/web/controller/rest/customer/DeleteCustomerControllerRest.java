package com.order.bachlinh.web.controller.rest.customer;

import com.order.bachlinh.web.component.dto.req.DeleteCustomerReq;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteCustomerControllerRest {
    private static final String DELETE_CUSTOMER = "/admin/customer/delete";

    private final CustomerService customerService;

    @PostMapping(path = DELETE_CUSTOMER)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCustomer(@RequestBody DeleteCustomerReq req) {
        customerService.deleteCustomer(req.customerId());
    }
}
