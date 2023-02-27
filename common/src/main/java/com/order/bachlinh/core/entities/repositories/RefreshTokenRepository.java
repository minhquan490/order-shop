package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.entities.model.RefreshToken;

public interface RefreshTokenRepository {

    RefreshToken getRefreshToken(String token);

    RefreshToken saveRefreshToken(RefreshToken token);

    RefreshToken updateRefreshToken(RefreshToken refreshToken);

    boolean deleteRefreshToken(RefreshToken refreshToken);
}