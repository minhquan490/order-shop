package com.order.bachlinh.web.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record RegisterDto(String message, @JsonIgnore boolean isError) {

}
