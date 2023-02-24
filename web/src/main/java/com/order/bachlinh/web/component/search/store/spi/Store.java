package com.order.bachlinh.web.component.search.store.spi;

import java.util.Map;

public interface Store {
    String getName();

    StoreDescriptor getDescriptor();

    boolean addValue(Map<String, String> value);
}
