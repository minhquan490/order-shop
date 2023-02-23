package com.order.bachlinh.core.component.search.store.spi;

import java.util.Map;

public interface StoreWriter {
    boolean write(Map<String, String> value);
}
