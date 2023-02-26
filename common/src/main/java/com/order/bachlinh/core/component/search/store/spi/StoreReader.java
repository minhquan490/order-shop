package com.order.bachlinh.core.component.search.store.spi;

import java.util.Collection;

public interface StoreReader {
    Collection<String> read(String keyWord);
}
