package com.order.bachlinh.core.component.search.index.internal;

import com.order.bachlinh.core.component.search.index.spi.IndexManagerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class IndexManagerFactoryBuilderProvider {
    public static IndexManagerFactory.Builder getFactoryBuilder() {
        return new SimpleIndexManagerFactory.SimpleIndexManagerFactoryBuilder();
    }
}
