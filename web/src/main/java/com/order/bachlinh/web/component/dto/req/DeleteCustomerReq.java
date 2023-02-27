package com.order.bachlinh.web.component.dto.req;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DeleteCustomerReq(@JsonAlias("customer-id") String customerId) {

}
