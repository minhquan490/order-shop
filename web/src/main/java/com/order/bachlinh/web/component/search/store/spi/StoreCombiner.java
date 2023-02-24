package com.order.bachlinh.web.component.search.store.spi;

import java.util.Collection;

public interface StoreCombiner {
    CombinedStore combineStore(String keyWord, Collection<SingularStore> stores);
}
