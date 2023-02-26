package com.order.bachlinh.core.component.search.store.spi;

import java.util.Collection;

public interface SingularStore extends Store {
    Collection<String> getStoreValue(String keyWord);
}
