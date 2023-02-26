package com.order.bachlinh.core.component.search.store.spi;

import com.order.bachlinh.core.entities.model.BaseEntity;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface StoreManager {

    CombinedStore combineStore(String keyWord, SingularStore... stores);

    CombinedStore combineStore(String keyWord, Collection<SingularStore> stores);

    SingularStore readStore(String storeName);

    boolean writeStore(String storeName, Map<String, String> value);

    SingularStore createStore(String name, String fileStorePath, Class<? extends BaseEntity> forEntity) throws IOException;

    boolean removeStore(StoreDescriptor storeDescriptor);
}
