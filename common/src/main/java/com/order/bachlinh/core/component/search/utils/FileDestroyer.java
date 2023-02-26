package com.order.bachlinh.core.component.search.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileDestroyer {

    public boolean remove(String path, String name) throws IOException {
        return Files.deleteIfExists(Path.of(path, name));
    }
}
