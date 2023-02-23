package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.store.spi.FileStoreType;
import com.order.bachlinh.core.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.core.component.search.store.spi.StoreDestroyer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
class DefaultStoreDestroyer implements StoreDestroyer {

    @Override
    public boolean destroy(StoreDescriptor storeDescriptor, FileStoreType storeType) {
        try {
            return Files.deleteIfExists(Path.of(URI.create(storeDescriptor.getFileStorePath())));
        } catch (IOException e) {
            String storePath = storeDescriptor.getFileStorePath();
            String storeName = storePath.split("/")[storePath.lastIndexOf("/")];
            log.error("Unable cleanup store {}", storeName);
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
