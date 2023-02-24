package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.web.component.search.store.spi.CombinedStore;
import com.order.bachlinh.web.component.search.store.spi.SingularStore;
import com.order.bachlinh.web.component.search.store.spi.StoreCombiner;
import com.order.bachlinh.core.entities.model.BaseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

class DefaultStoreCombiner implements StoreCombiner {
    @Override
    public CombinedStore combineStore(String keyword,Collection<SingularStore> stores) {
        List<MultiValueMap<Class<? extends BaseEntity>, String>> valueMaps = new ArrayList<>();
        stores.forEach(store -> {
            SingularStoreImplementer singularStoreImplementer = (SingularStoreImplementer) store;
            MultiValueMap<Class<? extends BaseEntity>, String> transfer = new MultiValueMapAdapter<>(new HashMap<>());
            transfer.put(singularStoreImplementer.getForEntity(), singularStoreImplementer.getStoreValue(keyword).stream().toList());
            valueMaps.add(transfer);
        });
        return new CombinedStoreImplementer(valueMaps);
    }
}
