package com.order.bachlinh.web.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.entities.model.RefreshToken;

public record LoginDto(RefreshToken refreshToken, String accessToken, @JsonIgnore boolean isLogged) {
}
