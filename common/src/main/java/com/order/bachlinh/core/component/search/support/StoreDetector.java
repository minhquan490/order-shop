package com.order.bachlinh.core.component.search.support;

import com.order.bachlinh.core.component.search.store.spi.StoreManager;
import org.springframework.context.ApplicationContext;

public interface StoreDetector {

    StoreManager configStore(ApplicationContext applicationContext);

    static StoreDetector simple() {
        return new SimpleStoreDetector();
    }
}
