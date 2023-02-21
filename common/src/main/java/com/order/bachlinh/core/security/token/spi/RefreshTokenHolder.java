package com.order.bachlinh.core.security.token.spi;

import com.order.bachlinh.core.entities.model.RefreshToken;

import java.util.Optional;

public class RefreshTokenHolder {
    private final Optional<RefreshToken> token;

    public RefreshTokenHolder(RefreshToken refreshToken) {
        this.token = Optional.ofNullable(refreshToken);
    }

    public boolean isNonNull() {
        return token.isPresent();
    }

    public RefreshToken getValue() {
        return token.get();
    }
}
