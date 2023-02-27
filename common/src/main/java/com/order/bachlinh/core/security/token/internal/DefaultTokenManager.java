package com.order.bachlinh.core.security.token.internal;

import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.security.token.spi.RefreshTokenGenerator;
import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.RefreshToken;
import com.order.bachlinh.core.entities.repositories.RefreshTokenRepository;
import com.order.bachlinh.core.security.token.spi.JwtDecoder;
import com.order.bachlinh.core.security.token.spi.JwtEncoder;
import com.order.bachlinh.core.security.token.spi.RefreshTokenHolder;
import com.order.bachlinh.core.security.token.spi.TokenManager;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class DefaultTokenManager implements TokenManager, RefreshTokenGenerator {

    private final JwtDecoder decoder;
    private final JwtEncoder encoder;
    private final EntityFactory entityFactory;
    private final RefreshTokenRepository refreshTokenRepository;

    DefaultTokenManager(String algorithm, String secretKey, ApplicationContext context) {
        this.decoder = JwtFactoryBuilderProvider.provideJwtDecoderFactoryBuilder()
                .algorithm(algorithm)
                .secretKey(secretKey)
                .build()
                .buildDecoder();
        this.encoder = JwtFactoryBuilderProvider.provideJwtEncoderFactoryBuilder()
                .algorithm(algorithm)
                .secretKey(secretKey)
                .build()
                .buildEncoder();
        this.entityFactory = context.getBean(EntityFactory.class);
        this.refreshTokenRepository = context.getBean(RefreshTokenRepository.class);
    }

    JwtDecoder getJwtDecoder() {
        return decoder;
    }

    JwtEncoder getJwtEncoder() {
        return encoder;
    }

    @Override
    public boolean isJwtExpired(String token) {
        Instant expiredAt = getJwtDecoder().decode(token).getExpiresAt();
        Assert.notNull(expiredAt, "Time expired is null");
        Instant now = Instant.now();
        return now.compareTo(expiredAt) > 0;
    }

    @Override
    public Map<String, Object> getClaimsFromToken(String token) {
        try {
            return getJwtDecoder().decode(token).getClaims();
        } catch (JwtException e) {
            return new HashMap<>(1);
        }
    }

    @Override
    public Map<String, Object> getHeadersFromToken(String token) {
        try {
            return getJwtDecoder().decode(token).getHeaders();
        } catch (JwtException e) {
            return new HashMap<>(1);
        }
    }

    @Override
    public RefreshTokenGenerator getRefreshTokenGenerator() {
        return this;
    }

    @Override
    public RefreshTokenHolder validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.getRefreshToken(token);
        if (refreshToken == null) {
            return new RefreshTokenHolder(null);
        }
        if (refreshToken.getTimeExpired().compareTo(Timestamp.from(Instant.now())) > 0) {
            return new RefreshTokenHolder(refreshToken);
        }
        return new RefreshTokenHolder(null);
    }

    @Override
    public Jwt decode(String token) {
        return getJwtDecoder().decode(token);
    }

    @Override
    public void encode(String key, Object value) {
        getJwtEncoder().encode(key, value);
    }

    @Override
    public void encode(Map<String, Object> payload) {
        getJwtEncoder().encode(payload);
    }

    @Override
    public String getTokenValue() {
        return getJwtEncoder().getTokenValue();
    }

    @Override
    public RefreshToken generateToken(Object customerId, String username) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RefreshToken refreshToken = entityFactory.getEntity(RefreshToken.class);
        refreshToken.setCustomer(customer);
        refreshToken.setId(entityFactory.getEntityContext(RefreshToken.class).getNextId());
        Instant timeCreated = Instant.now();
        refreshToken.setTimeCreated(Timestamp.from(timeCreated));
        refreshToken.setTimeExpired(Timestamp.from(Instant.ofEpochSecond(timeCreated.getEpochSecond() + 86400 * 365)));
        refreshToken.setRefreshTokenValue(UUID.randomUUID().toString());
        return refreshToken;
    }
}
