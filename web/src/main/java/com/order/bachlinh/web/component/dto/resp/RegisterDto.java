package com.order.bachlinh.web.component.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record RegisterDto(String message, @JsonIgnore boolean isError) {

}
