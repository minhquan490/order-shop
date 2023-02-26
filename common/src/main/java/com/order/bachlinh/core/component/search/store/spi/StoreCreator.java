package com.order.bachlinh.core.component.search.store.spi;

import com.order.bachlinh.core.entities.model.BaseEntity;

import java.io.IOException;

public interface StoreCreator {
    SingularStore createStore(String name, String fileStorePath, Class<? extends BaseEntity> forEntity) throws IOException;
}
