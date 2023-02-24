package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import com.order.bachlinh.web.component.search.store.spi.CombinedStore;
import com.order.bachlinh.web.component.search.store.spi.SingularStore;
import com.order.bachlinh.web.component.search.store.spi.StoreCombiner;
import com.order.bachlinh.web.component.search.store.spi.StoreContext;
import com.order.bachlinh.web.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.web.component.search.store.spi.StoreManager;
import com.order.bachlinh.core.entities.model.BaseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

class DefaultStoreManager implements StoreManager {
    private final StoreContext context;
    private final StoreCombiner storeCombiner;
    private final IndexManager indexManager;

    DefaultStoreManager(IndexManager indexManager) {
        this.context = new DefaultStoreContext();
        this.storeCombiner = new DefaultStoreCombiner();
        this.indexManager = indexManager;
    }

    @Override
    public CombinedStore combineStore(String keyword, SingularStore... stores) {
        return combineStore(keyword, Arrays.asList(stores));
    }

    @Override
    public CombinedStore combineStore(String keyword, Collection<SingularStore> stores) {
        return storeCombiner.combineStore(keyword, stores);
    }

    @Override
    public SingularStore readStore(String storeName) {
        return context.getStore(storeName);
    }

    @Override
    public boolean writeStore(String storeName, Map<String, String> value) {
        return readStore(storeName).addValue(value);
    }

    @Override
    public SingularStore createStore(String name, String fileStorePath, Class<? extends BaseEntity> forEntity) throws IOException {
        return context.addStore(name, fileStorePath, forEntity, indexManager);
    }

    @Override
    public boolean removeStore(StoreDescriptor storeDescriptor) {
        return context.removeStore(storeDescriptor);
    }
}
