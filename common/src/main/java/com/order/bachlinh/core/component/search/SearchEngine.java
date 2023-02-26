package com.order.bachlinh.core.component.search;

import com.order.bachlinh.core.annotation.Store;
import com.order.bachlinh.core.component.search.engine.EntityModelAnalyser;
import com.order.bachlinh.core.component.search.engine.KeywordCreator;
import com.order.bachlinh.core.component.search.store.spi.SingularStore;
import com.order.bachlinh.core.component.search.store.spi.StoreManager;
import com.order.bachlinh.core.component.search.support.StoreDetector;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.component.search.engine.EntitySearching;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SearchEngine implements EntitySearching, EntityModelAnalyser, KeywordCreator {
    private static final String SPLIT_PATTERN = "[!@#$%^&*()_+\\-=\\[\\]{};':\\\\|,.<>? ]";
    private final StoreManager storeManager;

    private SearchEngine(ApplicationContext applicationContext) {
        storeManager = StoreDetector.simple().configStore(applicationContext);
    }

    @Override
    public Collection<String> search(String storeName, String text) {
        List<String> results = new ArrayList<>();
        String[] keywords = text.split(SPLIT_PATTERN);
        SingularStore store = storeManager.readStore(storeName);
        for (String keyword : keywords) {
            results.addAll(store.getStoreValue(keyword));
        }
        return results;
    }

    @Override
    public Collection<String> searchIds(Class<? extends BaseEntity> entity, String text) {
        if (!entity.isAnnotationPresent(Store.class)) {
            return Collections.emptyList();
        }
        String storeName = entity.getAnnotation(Store.class).name();
        return search(storeName, text);
    }

    @Override
    public void analyze(String storeName, String text, String valueForKeyword) {
        String[] keywords = text.split(SPLIT_PATTERN);
        for (String keyword : keywords) {
            createKeyword(storeName, keyword, valueForKeyword);
        }
    }

    @Override
    public void analyze(String storeName, String text, BaseEntity entity) {
        analyze(storeName, text, String.valueOf(entity.getId()));
    }

    @Override
    public void createKeyword(String storeName, String keyword, String entityId) {
        Map<String, String> value = new HashMap<>();
        value.put(keyword, entityId);
        storeManager.writeStore(storeName, value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ApplicationContext applicationContext;

        public Builder applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public SearchEngine build() {
            return new SearchEngine(applicationContext);
        }
    }
}
