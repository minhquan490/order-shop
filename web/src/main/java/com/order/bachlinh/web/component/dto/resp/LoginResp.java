package com.order.bachlinh.web.component.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.entities.model.RefreshToken;

public record LoginResp(RefreshToken refreshToken, String accessToken, @JsonIgnore boolean isLogged) {
}
