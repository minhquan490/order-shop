package com.order.bachlinh.core.component.client.java.internal;

import com.order.bachlinh.core.component.client.java.spi.HttpClientFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class HttpClientFactoryBuilderProvider {

    public static HttpClientFactory.Builder getBuilder() {
        return new SimpleHttpClientFactory.SimpleHttpClientFactoryBuilder();
    }
}
