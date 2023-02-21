package com.order.bachlinh.web.services.spi.common;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.web.dto.form.LoginForm;
import com.order.bachlinh.web.dto.form.RegisterForm;
import com.order.bachlinh.web.dto.resp.LoginDto;
import com.order.bachlinh.web.dto.resp.RegisterDto;

public interface CustomerService {

    LoginDto login(LoginForm loginForm);

    RegisterDto register(RegisterForm registerForm);

    boolean logout(Customer customer);
}
