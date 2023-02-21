package com.order.bachlinh.core.repositories;

import jakarta.persistence.TypedQuery;

public interface HintDecorator {

    void applyCacheQueryHints(TypedQuery<?> query, String region);
}
