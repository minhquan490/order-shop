package com.order.bachlinh.core.component.search.store.internal;

import com.order.bachlinh.core.component.search.index.spi.IndexManager;
import com.order.bachlinh.core.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.core.component.search.store.spi.StoreReader;
import com.order.bachlinh.core.component.search.utils.FileReader;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

@Log4j2
class JsonStoreReader implements StoreReader {
    private final FileReader reader;
    private final IndexManager indexManager;

    public JsonStoreReader(StoreDescriptor storeDescriptor, IndexManager indexManager) {
        this.indexManager = indexManager;
        this.reader = new FileReader(storeDescriptor.getFileStorePath());
    }

    @Override
    public Collection<String> read(String keyword) {
        if (!indexManager.isExist(keyword)) {
            return Collections.emptyList();
        }
        long rootKeyIndex = indexManager.findIndex(keyword);
        long nextKeyIndex = indexManager.findIndex(indexManager.findNextKeywordExtract(keyword));
        rootKeyIndex += keyword.getBytes(StandardCharsets.UTF_8).length + 3;
        nextKeyIndex -= 2;
        try {
            return reader.read(rootKeyIndex, nextKeyIndex);
        } catch (IOException e) {
            log.error("Can not read file index store because IOException", e);
            return Collections.emptyList();
        }
    }
}
