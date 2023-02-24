package com.order.bachlinh.web.component.search.store.spi;

public interface StoreDescriptor {
    boolean isCombined();
    boolean isSingular();
    FileStoreType getFileStoreType();
    String getFileStorePath();
    String getName();
}
