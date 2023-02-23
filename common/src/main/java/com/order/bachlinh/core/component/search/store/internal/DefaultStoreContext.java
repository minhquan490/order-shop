package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.index.spi.IndexManager;
import com.order.bachlinh.core.component.search.store.spi.FileStoreType;
import com.order.bachlinh.core.component.search.store.spi.SingularStore;
import com.order.bachlinh.core.component.search.store.spi.StoreContext;
import com.order.bachlinh.core.component.search.store.spi.StoreCreator;
import com.order.bachlinh.core.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.core.component.search.store.spi.StoreDestroyer;
import com.order.bachlinh.core.entities.model.BaseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class DefaultStoreContext implements StoreContext {
    private final StoreDestroyer storeDestroyer;
    private final StoreCreator storeCreator;
    private final Map<String, SingularStore> stores = new ConcurrentHashMap<>();

    DefaultStoreContext() {
        this.storeDestroyer = new DefaultStoreDestroyer();
        this.storeCreator = new DefaultStoreCreator();
    }

    @Override
    public SingularStore getStore(String storeName) {
        return stores.get(storeName);
    }

    @Override
    public SingularStore addStore(String name,
                            String fileStorePath,
                            Class<? extends BaseEntity> forEntity,
                            IndexManager indexManager) throws IOException {
        SingularStore store = getStore(name);
        if (store != null) {
            return store;
        }
        store = storeCreator.createStore(name, fileStorePath, forEntity, indexManager);
        stores.put(name, store);
        return store;
    }

    @Override
    public boolean removeStore(StoreDescriptor storeDescriptor) {
        String storePath = storeDescriptor.getFileStorePath();
        String storeName = storePath.split("/")[storePath.lastIndexOf("/")];
        stores.remove(storeName);
        return storeDestroyer.destroy(storeDescriptor, FileStoreType.JSON);
    }
}
