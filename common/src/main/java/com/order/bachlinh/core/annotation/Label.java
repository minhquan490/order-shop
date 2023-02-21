package com.order.bachlinh.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation use for id prefix for identifier generation.
 *
 * @author Hoang Minh Quan
 * @see IdentifierGenerator IdentifierGenerator for detail.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Label {

    /**
     * Prefix value use for id column. Example {@code @Label("USR-")}
     */
    String value();
}
