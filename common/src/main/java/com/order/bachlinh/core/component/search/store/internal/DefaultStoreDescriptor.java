package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.store.spi.FileStoreType;
import com.order.bachlinh.core.component.search.store.spi.StoreDescriptor;

class DefaultStoreDescriptor implements StoreDescriptor {

    private final boolean isSingular;
    private final FileStoreType fileStoreType;
    private final String fileStorePath;

    DefaultStoreDescriptor(boolean useSingular, FileStoreType type, String storePath) {
        this.isSingular = useSingular;
        this.fileStorePath = storePath;
        this.fileStoreType = type;
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
}
