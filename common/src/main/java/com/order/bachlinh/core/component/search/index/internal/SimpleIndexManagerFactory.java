package com.order.bachlinh.core.component.search.index.internal;

import com.order.bachlinh.core.component.search.utils.FileReader;
import com.order.bachlinh.core.component.search.utils.FileWriter;
import com.order.bachlinh.core.component.search.index.spi.IndexLocator;
import com.order.bachlinh.core.component.search.index.spi.IndexManager;
import com.order.bachlinh.core.component.search.index.spi.IndexManagerFactory;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SimpleIndexManagerFactory implements IndexManagerFactory {
    private final String name;
    private final String path;

    @Override
    public IndexManager getIndexManager() {
        IndexPositionContainer container = new IndexPositionContainer();
        String pathName = Path.of(path, name).toString();
        FileWriter fileWriter = new FileWriter(pathName);
        FileReader fileReader = new FileReader(pathName);
        IndexLocator locator = new SimpleIndexLocator(fileReader, container);
        return new DefaultIndexManager(locator, fileWriter, fileReader);
    }

    static class SimpleIndexManagerFactoryBuilder implements IndexManagerFactory.Builder {
        private String name;
        private String path;

        @Override
        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder path(@NonNull String path) {
            this.path = path;
            return this;
        }

        @Override
        public IndexManagerFactory build() {
            return new SimpleIndexManagerFactory(name, path);
        }
    }
}
