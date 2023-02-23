package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.index.spi.IndexManager;
import com.order.bachlinh.core.component.search.store.spi.FileStoreType;
import com.order.bachlinh.core.component.search.store.spi.SingularStore;
import com.order.bachlinh.core.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.core.component.search.store.spi.StoreReader;
import com.order.bachlinh.core.component.search.store.spi.StoreWriter;
import com.order.bachlinh.core.entities.model.BaseEntity;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.Map;

@Log4j2
class SingularStoreImplementer implements SingularStore {
    private final Class<? extends BaseEntity> forEntity;
    private final String name;
    private final StoreDescriptor storeDescriptor;
    private final StoreReader storeReader;
    private final StoreWriter storeWriter;

    SingularStoreImplementer(String name, String fileStorePath, Class<? extends BaseEntity> forEntity, IndexManager indexManager) {
        this.name = name;
        this.storeDescriptor = new DefaultStoreDescriptor(true, FileStoreType.JSON, fileStorePath + "/" + name);
        this.storeWriter = buildStoreWriter(storeDescriptor, indexManager);
        this.storeReader = buildStoreReader(storeDescriptor, indexManager);
        this.forEntity = forEntity;
    }

    @Override
    public Collection<String> getStoreValue(String keyWord) {
        return storeReader.read(keyWord);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StoreDescriptor getDescriptor() {
        return storeDescriptor;
    }

    @Override
    public boolean addValue(Map<String, String> value) {
        return storeWriter.write(value);
    }

    public Class<? extends BaseEntity> getForEntity() {
        return forEntity;
    }

    private StoreWriter buildStoreWriter(StoreDescriptor storeDescriptor, IndexManager indexManager) {
        return switch (storeDescriptor.getFileStoreType()) {
            case JSON -> new JsonStoreWriter(storeDescriptor, indexManager);
            case XML -> throw new UnsupportedOperationException("XML file is not supported");
            case DATABASE -> throw new UnsupportedOperationException("Database is not supported");
        };
    }

    private StoreReader buildStoreReader(StoreDescriptor storeDescriptor, IndexManager indexManager) {
        return switch (storeDescriptor.getFileStoreType()) {
            case JSON -> new JsonStoreReader(storeDescriptor, indexManager);
            case XML -> throw new UnsupportedOperationException("XML file is not supported");
            case DATABASE -> throw new UnsupportedOperationException("Database is not supported");
        };
    }
}

