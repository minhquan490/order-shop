package com.order.bachlinh.core.security.token.internal;

import com.order.bachlinh.core.security.token.spi.JwtDecoder;
import com.order.bachlinh.core.security.token.spi.JwtDecoderFactory;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class JwtDecoderFactoryImpl implements JwtDecoderFactory {

    private final org.springframework.security.oauth2.jwt.JwtDecoder decoder;

    JwtDecoderFactoryImpl(String algorithm, String secretKey) {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), algorithm);
        this.decoder = NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Override
    public JwtDecoder buildDecoder() {
        return new DefaultJwtDecoder(decoder);
    }
}
