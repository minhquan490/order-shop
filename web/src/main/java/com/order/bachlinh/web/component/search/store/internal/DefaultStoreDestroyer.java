package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.web.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.web.component.search.store.spi.StoreDestroyer;
import com.order.bachlinh.web.component.search.utils.FileDestroyer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
class DefaultStoreDestroyer implements StoreDestroyer {

    @Override
    public boolean destroy(StoreDescriptor storeDescriptor) {
        try {
            return new FileDestroyer().remove(storeDescriptor.getFileStorePath(), storeDescriptor.getName());
        } catch (IOException e) {
            String storeName = storeDescriptor.getName().split(getExtension(storeDescriptor))[0];
            log.error("Unable cleanup store {}", storeName);
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private String getExtension(StoreDescriptor storeDescriptor) {
        return switch (storeDescriptor.getFileStoreType()) {
            case JSON -> ".json";
            case XML -> ".xml";
            case DATABASE -> throw new UnsupportedOperationException("Database is not supported");
        };
    }
}
