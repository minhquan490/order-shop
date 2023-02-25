package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.web.component.search.store.spi.SingularStore;
import com.order.bachlinh.web.component.search.store.spi.StoreCreator;

class DefaultStoreCreator implements StoreCreator {
    @Override
    public SingularStore createStore(String name, String fileStorePath, Class<? extends BaseEntity> forEntity) {
        return new SingularStoreImplementer(name, fileStorePath, forEntity);
    }
}
