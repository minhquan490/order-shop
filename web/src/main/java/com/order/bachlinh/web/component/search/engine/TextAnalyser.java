package com.order.bachlinh.web.component.search.engine;

import com.order.bachlinh.core.entities.model.BaseEntity;

public interface TextAnalyser {
    void analyze(String storeName, String text, BaseEntity entity);
}
