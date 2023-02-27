package com.order.bachlinh.web.component.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record RegisterResp(String message, @JsonIgnore boolean isError) {

}
