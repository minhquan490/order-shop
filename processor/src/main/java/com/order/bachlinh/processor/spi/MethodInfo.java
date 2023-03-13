package com.order.bachlinh.processor.spi;

import java.lang.annotation.Annotation;

public interface MethodInfo {
    String getName();
    Class<?> getReturnType();
    Class<?>[] getParameterTypes();
    Annotation[] getAnnotations();
}
