package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.index.spi.IndexManager;
import com.order.bachlinh.core.component.search.store.spi.SingularStore;
import com.order.bachlinh.core.component.search.store.spi.StoreCreator;
import com.order.bachlinh.core.entities.model.BaseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class DefaultStoreCreator implements StoreCreator {
    @Override
    public SingularStore createStore(String name, String fileStorePath, Class<? extends BaseEntity> forEntity, IndexManager indexManager) throws IOException {
        Files.createFile(Path.of(fileStorePath, name));
        return new SingularStoreImplementer(name, fileStorePath, forEntity, indexManager);
    }
}
