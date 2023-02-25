package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.web.component.search.store.spi.FileStoreType;
import com.order.bachlinh.web.component.search.store.spi.SingularStore;
import com.order.bachlinh.web.component.search.store.spi.StoreContext;
import com.order.bachlinh.web.component.search.store.spi.StoreCreator;
import com.order.bachlinh.web.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.web.component.search.store.spi.StoreDestroyer;

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
                            Class<? extends BaseEntity> forEntity) throws IOException {
        SingularStore store = getStore(name);
        if (store != null) {
            return store;
        }
        store = storeCreator.createStore(name, fileStorePath, forEntity);
        stores.put(name, store);
        return store;
    }

    @Override
    public boolean removeStore(StoreDescriptor storeDescriptor) {
        stores.remove(storeDescriptor.getName().split(getExtension(storeDescriptor.getFileStoreType()))[0]);
        return storeDestroyer.destroy(storeDescriptor);
    }

    private String getExtension(FileStoreType type) {
        return switch (type) {
            case JSON -> ".json";
            case XML -> ".xml";
            case DATABASE -> throw new UnsupportedOperationException("Database is not supported");
        };
    }
}
