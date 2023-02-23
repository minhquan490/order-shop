package com.order.bachlinh.core.component.search.store.spi;

import java.util.Map;

public interface Store {
    String getName();

    StoreDescriptor getDescriptor();

    boolean addValue(Map<String, String> value);
}
