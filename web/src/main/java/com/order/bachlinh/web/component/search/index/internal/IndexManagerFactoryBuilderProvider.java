package com.order.bachlinh.web.component.search.index.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexManagerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class IndexManagerFactoryBuilderProvider {
    public static IndexManagerFactory.Builder getFactoryBuilder() {
        return new SimpleIndexManagerFactory.SimpleIndexManagerFactoryBuilder();
    }
}
