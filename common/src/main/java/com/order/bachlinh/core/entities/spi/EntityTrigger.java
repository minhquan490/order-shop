package com.order.bachlinh.core.entities.spi;

import com.order.bachlinh.core.entities.model.BaseEntity;

public interface EntityTrigger {
    void execute(BaseEntity entity);

    TriggerMode getMode();
}
