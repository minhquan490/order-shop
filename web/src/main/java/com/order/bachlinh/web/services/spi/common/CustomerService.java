package com.order.bachlinh.web.services.spi.common;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.web.component.dto.form.LoginForm;
import com.order.bachlinh.web.component.dto.form.RegisterForm;
import com.order.bachlinh.web.component.dto.resp.CustomerResp;
import com.order.bachlinh.web.component.dto.resp.CustomerInformationResp;
import com.order.bachlinh.web.component.dto.resp.LoginResp;
import com.order.bachlinh.web.component.dto.resp.RegisterResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    LoginResp login(LoginForm loginForm);

    RegisterResp register(RegisterForm registerForm);

    boolean logout(Customer customer);

    void deleteCustomer(String customerId);

    CustomerInformationResp getCustomerInformation(String customerId);

    Page<CustomerResp> getFullInformationOfCustomer(Pageable pageable);
}
