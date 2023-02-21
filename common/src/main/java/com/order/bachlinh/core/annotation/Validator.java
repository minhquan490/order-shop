package com.order.bachlinh.core.annotation;

import com.order.bachlinh.core.entities.spi.EntityValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation use for specify validators will apply to target
 *
 * @author Hoang Minh Quan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Validator {

    /**
     * Validators apply to
     */
    Class<? extends EntityValidator>[] validators();
}
