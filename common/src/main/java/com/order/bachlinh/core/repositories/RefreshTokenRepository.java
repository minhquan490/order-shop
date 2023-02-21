package com.order.bachlinh.core.repositories;

import com.order.bachlinh.core.entities.model.RefreshToken;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository {

    RefreshToken getRefreshToken(String token);

    @Transactional(propagation = Propagation.NESTED)
    RefreshToken saveRefreshToken(RefreshToken token);

    RefreshToken updateRefreshToken(RefreshToken refreshToken);

    boolean deleteRefreshToken(RefreshToken refreshToken);
}