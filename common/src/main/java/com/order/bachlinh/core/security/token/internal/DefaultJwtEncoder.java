package com.order.bachlinh.core.security.token.internal;

import com.order.bachlinh.core.security.token.spi.JwtEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

class DefaultJwtEncoder implements JwtEncoder {
    private static final int MINUS_EXPIRE = 60;
    private final org.springframework.security.oauth2.jwt.JwtEncoder encoder;
    private final Map<String, Object> internalProp;
    private final String header;

    DefaultJwtEncoder(org.springframework.security.oauth2.jwt.JwtEncoder encoder, String header) {
        this.encoder = encoder;
        this.internalProp = new HashMap<>();
        this.header = header;
    }

    @Override
    public void encode(String key, Object value) {
        this.internalProp.put(key, value);
    }

    @Override
    public void encode(Map<String, Object> payload) {
        this.internalProp.putAll(payload);
    }

    @Override
    public String getTokenValue() {
        Map<String, Object> copyProp = new HashMap<>(internalProp);
        internalProp.clear();
        return encoder.encode(JwtEncoderParameters.from(buildHeader(), buildClaimsSet(copyProp))).getTokenValue();
    }

    private Instant calculateJwtExpiredTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(MINUS_EXPIRE).toInstant(ZoneOffset.UTC);
    }

    private JwtClaimsSet buildClaimsSet(Map<String, Object> payload) {
        return JwtClaimsSet.builder()
                .claims(p -> p.putAll(payload))
                .expiresAt(calculateJwtExpiredTime())
                .build();
    }

    private JwsHeader buildHeader() {
        return JwsHeader.with(MacAlgorithm.from(header)).build();
    }
}
