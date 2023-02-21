package com.order.bachlinh.core.entities.spi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

/**
 * Base validator for all entities validator use in this project.
 *
 * @author Hoang Minh Quan
 * */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractValidator implements EntityValidator {
    private ApplicationContext context;

    protected ApplicationContext getApplicationContext() {
        return context;
    }
}
