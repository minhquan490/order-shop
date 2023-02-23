package com.order.bachlinh.core.component.search.store.spi;

import java.util.Collection;

public interface StoreCombiner {
    CombinedStore combineStore(String keyWord, Collection<SingularStore> stores);
}
