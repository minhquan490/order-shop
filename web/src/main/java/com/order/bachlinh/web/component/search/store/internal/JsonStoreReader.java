package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import com.order.bachlinh.web.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.web.component.search.store.spi.StoreReader;
import com.order.bachlinh.web.component.search.utils.FileReader;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
        long nextKeyIndex = indexManager.findNextKeyword(keyword);
        rootKeyIndex += keyword.getBytes(StandardCharsets.UTF_8).length + 3;
        nextKeyIndex -= 2;
        try {
            String result = reader.read(rootKeyIndex, nextKeyIndex).split(":")[1];
            result = result
                    .replace("[", "")
                    .replace("]", "");
            return Arrays.asList(result.split(","));
        } catch (IOException e) {
            log.error("Can not read file index store because IOException", e);
            return Collections.emptyList();
        }
    }
}
