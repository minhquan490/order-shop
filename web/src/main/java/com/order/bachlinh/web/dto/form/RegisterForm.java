package com.order.bachlinh.web.dto.form;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.Gender;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterForm(String username,
                           String firstName,
                           String lastName,
                           String phoneNumber,
                           String email,
                           String gender,
                           String password) {
    public Customer toCustomer(EntityFactory entityFactory, PasswordEncoder passwordEncoder) {
        Customer customer = entityFactory.getEntity(Customer.class);
        customer.setUsername(this.username());
        customer.setFirstName(this.firstName());
        customer.setLastName(this.lastName());
        customer.setEmail(this.email());
        customer.setPhoneNumber(this.phoneNumber());
        customer.setPassword(passwordEncoder.encode(this.password()));
        customer.setGender(Gender.valueOf(this.gender().toUpperCase()).name());
        return customer;
    }
}
