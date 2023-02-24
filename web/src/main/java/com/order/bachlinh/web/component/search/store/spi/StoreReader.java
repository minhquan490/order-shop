package com.order.bachlinh.web.component.search.store.spi;

import java.util.Collection;

public interface StoreReader {
    Collection<String> read(String keyWord);
}
