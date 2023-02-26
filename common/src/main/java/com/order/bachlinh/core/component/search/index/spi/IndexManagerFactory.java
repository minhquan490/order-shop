package com.order.bachlinh.core.component.search.index.spi;

import lombok.NonNull;

public interface IndexManagerFactory {
    IndexManager getIndexManager();

    interface Builder {
        Builder name(@NonNull String name);
        Builder path(@NonNull String path);
        IndexManagerFactory build();
    }
}
