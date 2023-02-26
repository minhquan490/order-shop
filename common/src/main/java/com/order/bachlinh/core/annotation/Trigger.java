package com.order.bachlinh.core.annotation;

import com.order.bachlinh.core.entities.spi.EntityTrigger;
import com.order.bachlinh.core.entities.spi.TriggerMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Trigger {
    Class<? extends EntityTrigger>[] triggers();

    TriggerMode mode();
}
