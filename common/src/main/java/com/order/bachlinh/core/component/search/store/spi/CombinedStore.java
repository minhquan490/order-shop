package com.order.bachlinh.core.component.search.store.spi;

import com.order.bachlinh.core.entities.model.BaseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Collection;

public interface CombinedStore extends Store {
    Collection<MultiValueMap<Class<? extends BaseEntity>, String>> getStoreValues();
}
