package com.order.bachlinh.web.component.search;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.web.component.search.engine.KeywordCreator;
import com.order.bachlinh.web.component.search.engine.Searching;
import com.order.bachlinh.web.component.search.engine.TextAnalyser;
import com.order.bachlinh.web.component.search.store.spi.SingularStore;
import com.order.bachlinh.web.component.search.store.spi.StoreManager;
import com.order.bachlinh.web.component.search.support.StoreDetector;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SearchEngine implements Searching, TextAnalyser, KeywordCreator {
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
    public void analyze(String storeName, String text, BaseEntity entity) {
        String[] keywords = text.split(SPLIT_PATTERN);
        for (String keyword : keywords) {
            createKeyword(storeName, keyword, String.valueOf(entity.getId()));
        }
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
