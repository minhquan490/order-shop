package com.order.bachlinh.web.component.search.store.spi;

import java.util.Collection;

public interface SingularStore extends Store {
    Collection<String> getStoreValue(String keyWord);
}
