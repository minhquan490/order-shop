package com.order.bachlinh.web.component.search.store.spi;

public interface StoreManagerFactory {
    StoreManager getStoreManager();

    interface Builder {
        StoreManagerFactory build();
    }
}
