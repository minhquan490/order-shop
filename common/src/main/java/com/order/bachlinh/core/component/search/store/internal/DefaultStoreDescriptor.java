package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.store.spi.FileStoreType;
import com.order.bachlinh.core.component.search.store.spi.StoreDescriptor;

class DefaultStoreDescriptor implements StoreDescriptor {

    private final boolean isSingular;
    private final FileStoreType fileStoreType;
    private final String fileStorePath;
    private final String name;

    DefaultStoreDescriptor(boolean useSingular, FileStoreType type, String storePath, String name) {
        this.isSingular = useSingular;
        this.fileStorePath = storePath;
        this.fileStoreType = type;
        this.name = obtainStoreName(name, type);
    }
    @Override
    public boolean isCombined() {
        return !isSingular();
    }

    @Override
    public boolean isSingular() {
        return isSingular;
    }

    @Override
    public FileStoreType getFileStoreType() {
        return fileStoreType;
    }

    @Override
    public String getFileStorePath() {
        return fileStorePath;
    }

    @Override
    public String getName() {
        return name;
    }

    private String obtainStoreName(String rawName, FileStoreType type) {
        return switch (type) {
            case JSON -> rawName.concat(".json");
            case XML -> rawName.concat(".xml");
            case DATABASE -> throw new UnsupportedOperationException("Database is not supported");
        };
    }
}
