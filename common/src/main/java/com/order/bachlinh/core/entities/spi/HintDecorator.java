package com.order.bachlinh.core.entities.spi;

import jakarta.persistence.TypedQuery;

public interface HintDecorator {

    void applyCacheQueryHints(TypedQuery<?> query, String region);
}
