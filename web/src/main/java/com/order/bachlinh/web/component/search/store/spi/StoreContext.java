package com.order.bachlinh.web.component.search.store.spi;

import com.order.bachlinh.core.entities.model.BaseEntity;

import java.io.IOException;

public interface StoreContext {
    SingularStore getStore(String storeName);
    SingularStore addStore(String name, String fileStorePath, Class<? extends BaseEntity> forEntity) throws IOException;
    boolean removeStore(StoreDescriptor storeDescriptor);
}
