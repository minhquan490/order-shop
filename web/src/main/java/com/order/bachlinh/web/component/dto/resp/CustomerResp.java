package com.order.bachlinh.web.component.dto.resp;

import com.order.bachlinh.core.entities.model.Customer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter(AccessLevel.PRIVATE)
public class CustomerResp {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String gender;
    private String role;
    private String username;
    private Collection<String> address;
    private boolean activated;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public static CustomerResp toDto(Customer customer) {
        CustomerResp dto = new CustomerResp();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setEmail(customer.getEmail());
        dto.setGender(customer.getGender().toLowerCase());
        dto.setUsername(customer.getUsername());
        dto.setAddress(customer.getAddresses().stream().map(a -> String.join(",", a.getValue(), a.getCity(), a.getCountry())).toList());
        dto.setActivated(customer.isActivated());
        dto.setAccountNonExpired(customer.isAccountNonExpired());
        dto.setAccountNonLocked(customer.isAccountNonLocked());
        dto.setCredentialsNonExpired(customer.isCredentialsNonExpired());
        dto.setEnabled(customer.isEnabled());
        return dto;
    }
}
