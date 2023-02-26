package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.store.spi.StoreManager;
import com.order.bachlinh.core.component.search.store.spi.StoreManagerFactory;

class SimpleStoreManagerFactory implements StoreManagerFactory {

    SimpleStoreManagerFactory() {
    }

    @Override
    public StoreManager getStoreManager() {
        return new DefaultStoreManager();
    }

    static class SimpleStoreManagerFactoryBuilder implements StoreManagerFactory.Builder {

        @Override
        public StoreManagerFactory build() {
            return new SimpleStoreManagerFactory();
        }
    }
}
