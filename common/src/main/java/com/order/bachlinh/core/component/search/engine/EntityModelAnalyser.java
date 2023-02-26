package com.order.bachlinh.core.component.search.engine;

import com.order.bachlinh.core.entities.model.BaseEntity;

public interface EntityModelAnalyser extends TextAnalyser {
    void analyze(String storeName, String text, BaseEntity entity);
}
