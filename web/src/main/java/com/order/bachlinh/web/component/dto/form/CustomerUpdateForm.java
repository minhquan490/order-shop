package com.order.bachlinh.web.component.dto.form;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonRootName("customer")
@Setter
public class CustomerUpdateForm {

    @JsonAlias("id")
    private String id;

    @JsonAlias("first-name")
    private String firstName;

    @JsonAlias("last-name")
    private String lastName;

    @JsonAlias("phone")
    private String phoneNumber;

    @JsonAlias("email")
    private String email;
}
