package com.order.bachlinh.core.component.search.engine;

import com.order.bachlinh.core.entities.model.BaseEntity;

import java.util.Collection;

public interface EntitySearching extends Searching {
    Collection<String> searchIds(Class<? extends BaseEntity> entity, String text);
}
