package com.order.bachlinh.web.component.dto.form;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

@Getter
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
}
