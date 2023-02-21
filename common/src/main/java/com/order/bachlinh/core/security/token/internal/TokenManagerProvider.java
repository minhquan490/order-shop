package com.order.bachlinh.core.security.token.internal;

import com.order.bachlinh.core.security.token.spi.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
public class TokenManagerProvider {
    private final String algorithm;
    private final String secretKey;
    private final ApplicationContext context;

    public TokenManager getTokenManager() {
        return new DefaultTokenManager(algorithm, secretKey, context);
    }
}
