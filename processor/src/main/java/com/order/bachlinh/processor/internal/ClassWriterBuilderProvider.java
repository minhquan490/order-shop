package com.order.bachlinh.processor.internal;

import com.order.bachlinh.processor.spi.ClassWriter;

public final class ClassWriterBuilderProvider {
    private ClassWriterBuilderProvider() {}

    public static ClassWriter.Builder getClassWriterBuilder() {
        return DefaultClassWriter.builder();
    }
}
