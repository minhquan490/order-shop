package com.order.bachlinh.web.component.dto.form;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.order.bachlinh.core.entities.model.Cart;
import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.Gender;
import com.order.bachlinh.core.entities.model.Role;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@JsonRootName("customer")
@Setter
public class CreateCustomerForm {

    @JsonAlias("username")
    private String username;

    @JsonAlias("password")
    private String password;

    @JsonAlias("first-name")
    private String firstName;

    @JsonAlias("last-name")
    private String lastName;

    @JsonAlias("phone")
    private String phoneNumber;

    @JsonAlias("email")
    private String email;

    @JsonAlias("gender")
    private String gender;

    public static Customer toCustomer(CreateCustomerForm form, EntityFactory factory, PasswordEncoder encoder) {
        Customer customer = factory.getEntity(Customer.class);
        customer.setUsername(form.getUsername());
        customer.setPassword(encoder.encode(form.getPassword()));
        customer.setFirstName(form.getFirstName());
        customer.setLastName(form.getLastName());
        customer.setPhoneNumber(form.phoneNumber);
        customer.setEmail(form.getEmail());
        customer.setGender(Gender.of(form.getGender()).name());
        customer.setRole(Role.of("admin").name());
        Cart cart = factory.getEntity(Cart.class);
        cart.setCustomer(customer);
        customer.setCart(cart);
        return customer;
    }
}
