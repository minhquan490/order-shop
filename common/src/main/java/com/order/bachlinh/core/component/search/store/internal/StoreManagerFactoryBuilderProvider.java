package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.store.spi.StoreManagerFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public final class StoreManagerFactoryBuilderProvider {
    public static StoreManagerFactory.Builder getStoreManagerFactoryBuilder() {
        return new SimpleStoreManagerFactory.SimpleStoreManagerFactoryBuilder();
    }
}
