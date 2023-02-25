package com.order.bachlinh.web.component.search.support;

import com.order.bachlinh.core.annotation.Store;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.exception.CriticalException;
import com.order.bachlinh.core.util.EntityUtils;
import com.order.bachlinh.web.component.search.store.internal.StoreManagerFactoryBuilderProvider;
import com.order.bachlinh.web.component.search.store.spi.StoreManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class SimpleStoreDetector implements StoreDetector {

    @Override
    @SuppressWarnings("unchecked")
    public StoreManager configStore(ApplicationContext applicationContext) {
        StoreManager storeManager = StoreManagerFactoryBuilderProvider
                .getStoreManagerFactoryBuilder()
                .build()
                .getStoreManager();
        try {
            Collection<Class<?>> entities = EntityUtils
                    .scanPackageEntity(applicationContext)
                    .stream()
                    .filter(entity -> entity.isAnnotationPresent(Store.class))
                    .toList();
            for (Class<?> entity : entities) {
                Store store = entity.getAnnotation(Store.class);
                String storePath = getPath(store);
                storeManager.createStore(store.name(), storePath, (Class<? extends BaseEntity>) entity);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new CriticalException("Can not find entities", e);
        }
        return storeManager;
    }

    private String getPath(Store store) {
        if (store.saveLocation().startsWith("project:")) {
            return new File("")
                    .getAbsolutePath()
                    .concat("/")
                    .concat(store.saveLocation().split("project:")[1]);
        }
        return store.saveLocation();
    }
}
