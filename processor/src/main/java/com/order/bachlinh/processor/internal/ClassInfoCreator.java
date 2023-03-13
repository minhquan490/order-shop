package com.order.bachlinh.processor.internal;

import com.order.bachlinh.processor.spi.ClassInfo;

public final class ClassInfoCreator {
    private ClassInfoCreator() {}

    public static ClassInfo createClassInfo(Class<?> controller) {
        return new DefaultClassInfo(controller);
    }
}
