package com.order.bachlinh.web.controller.rest.customer;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.security.token.spi.PrincipalHolder;
import com.order.bachlinh.web.component.dto.resp.CustomerInformationResp;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerInformationControllerRest {
    private static final String CUSTOMER_INFORMATION_URL = "/customer/information";
    private final CustomerService customerService;

    @GetMapping(path = CUSTOMER_INFORMATION_URL)
    public ResponseEntity<CustomerInformationResp> getCustomerInformation() {
        PrincipalHolder principalHolder = (PrincipalHolder) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerInformationResp resp = customerService.getCustomerInformation(((Customer) principalHolder.getPrincipal()).getId());
        return ResponseEntity.ok(resp);
    }
}
