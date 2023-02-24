package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import com.order.bachlinh.web.component.search.store.spi.StoreManager;
import com.order.bachlinh.web.component.search.store.spi.StoreManagerFactory;

class SimpleStoreManagerFactory implements StoreManagerFactory {
    private final IndexManager indexManager;

    SimpleStoreManagerFactory(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    @Override
    public StoreManager getStoreManager() {
        return new DefaultStoreManager(indexManager);
    }

    static class SimpleStoreManagerFactoryBuilder implements StoreManagerFactory.Builder {
        private IndexManager indexManager;

        @Override
        public Builder indexManager(IndexManager indexManager) {
            this.indexManager = indexManager;
            return this;
        }

        @Override
        public StoreManagerFactory build() {
            return new SimpleStoreManagerFactory(indexManager);
        }
    }
}
