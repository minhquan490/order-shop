package com.order.bachlinh.core.annotation;

import com.order.bachlinh.core.entities.spi.IdContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation use on the factory method of entity creation.
 * After method entity creation was marked then the id interceptor
 * will generate id with prefix in a {@link Label}
 *
 * @author Hoang Minh Quan
 * @see IdContext IdContext for detail.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IdentifierGenerator {
}
