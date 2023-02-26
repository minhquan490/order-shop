package com.order.bachlinh.core.entities.spi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTrigger implements EntityTrigger {
    private ApplicationContext applicationContext;
    private TriggerMode mode;

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public TriggerMode getMode() {
        return mode;
    }
}
