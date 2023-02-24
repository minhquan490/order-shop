package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.store.spi.CombinedStore;
import com.order.bachlinh.core.component.search.store.spi.FileStoreType;
import com.order.bachlinh.core.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.core.entities.model.BaseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

class CombinedStoreImplementer implements CombinedStore {
    List<MultiValueMap<Class<? extends BaseEntity>, String>> values;
    private final StoreDescriptor storeDescriptor;

    CombinedStoreImplementer(List<MultiValueMap<Class<? extends BaseEntity>, String>> values) {
        this.values = values;
        this.storeDescriptor = new DefaultStoreDescriptor(false, FileStoreType.JSON, "", "");
    }

    @Override
    public Collection<MultiValueMap<Class<? extends BaseEntity>, String>> getStoreValues() {
        return values;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public StoreDescriptor getDescriptor() {
        return storeDescriptor;
    }

    @Override
    public boolean addValue(Map<String, String> value) {
        // Unsupported
        return false;
    }
}
