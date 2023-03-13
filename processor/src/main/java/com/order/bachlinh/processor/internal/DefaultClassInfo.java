package com.order.bachlinh.processor.internal;

import com.order.bachlinh.processor.spi.ClassInfo;
import com.order.bachlinh.processor.spi.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

final class DefaultClassInfo implements ClassInfo {
    private final Class<?> controller;
    private final Collection<MethodInfo> methodInfo;

    DefaultClassInfo(Class<?> controller) {
        this.controller = controller;
        this.methodInfo = defineMethodInfo(controller);
    }

    @Override
    public Package getPackage() {
        return controller.getPackage();
    }

    @Override
    public String getName() {
        return controller.getSimpleName();
    }

    @Override
    public Annotation[] getAnnotations() {
        return controller.getAnnotations();
    }

    @Override
    public Collection<MethodInfo> getMethods() {
        return methodInfo;
    }

    private Collection<MethodInfo> defineMethodInfo(Class<?> controller) {
        Method[] methods = controller.getMethods();
        return Set.of(methods)
                .stream()
                .map(DefaultMethodInfo::new)
                .map(MethodInfo.class::cast)
                .toList();
    }
}
