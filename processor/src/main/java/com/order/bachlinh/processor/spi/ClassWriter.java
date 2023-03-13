package com.order.bachlinh.processor.spi;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;

public interface ClassWriter {

    void write() throws IOException;

    interface Builder {
        Builder classInfo(Collection<ClassInfo> classInfo);
        Builder filer(Filer filer);
        ClassWriter build();
    }
}
