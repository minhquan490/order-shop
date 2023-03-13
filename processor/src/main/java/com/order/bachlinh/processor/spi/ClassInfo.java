package com.order.bachlinh.processor.spi;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface ClassInfo {
    Package getPackage();
    String getName();
    Annotation[] getAnnotations();
    Collection<MethodInfo> getMethods();
}
