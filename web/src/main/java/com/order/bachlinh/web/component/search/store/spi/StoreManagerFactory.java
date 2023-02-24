package com.order.bachlinh.web.component.search.store.spi;

import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import com.order.bachlinh.web.component.search.store.internal.StoreManagerFactoryBuilderProvider;

public interface StoreManagerFactory {
    StoreManager getStoreManager();

    static Builder builder() {
        return StoreManagerFactoryBuilderProvider.getStoreManagerFactoryBuilder();
    }

    interface Builder {
        Builder indexManager(IndexManager indexManager);
        StoreManagerFactory build();
    }
}
