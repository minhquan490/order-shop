package com.order.bachlinh.processor.internal;

import com.order.bachlinh.processor.spi.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class DefaultMethodInfo implements MethodInfo {
    private final Method method;

    DefaultMethodInfo(Method method) {
        this.method = method;
    }
    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Annotation[] getAnnotations() {
        return method.getAnnotations();
    }
}
