package com.order.bachlinh.core.component.search.store.spi;

public interface StoreDescriptor {
    boolean isCombined();
    boolean isSingular();
    FileStoreType getFileStoreType();
    String getFileStorePath();
}
