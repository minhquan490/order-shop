package com.order.bachlinh.core.component.search.store.spi;

public interface StoreManagerFactory {
    StoreManager getStoreManager();

    interface Builder {
        StoreManagerFactory build();
    }
}
