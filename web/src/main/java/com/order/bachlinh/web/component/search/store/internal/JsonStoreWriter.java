package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import com.order.bachlinh.web.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.web.component.search.store.spi.StoreWriter;
import com.order.bachlinh.web.component.search.utils.FileWriter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
class JsonStoreWriter implements StoreWriter {
    private static final char COMMA = ',';
    private static final char DOUBLE_QUOTE = '"';
    private static final char COLON = ':';
    private static final char LEFT_SQUARE_BRACKET = '[';
    private static final char RIGHT_SQUARE_BRACKET = ']';
    private final IndexManager indexManager;
    private final FileWriter fileWriter;

    public JsonStoreWriter(StoreDescriptor storeDescriptor, IndexManager indexManager) {
        this.indexManager = indexManager;
        this.fileWriter = new FileWriter(storeDescriptor.getFileStorePath());
    }

    @Override
    public boolean write(Map<String, String> value) {
        AtomicInteger result = new AtomicInteger(0);
        value.forEach((key, value1) -> result.set(result.get() + write(key, value1)));
        return result.get() != 0;
    }

    private int write(String keyword, String entityId) {
        StringBuilder writableValue = new StringBuilder();
        long keyWordIndex = indexManager.findIndex(keyword);
        if (keyWordIndex < 0) {
            keyWordIndex = fileWriter.getFileSize() == 0 ? 0 : fileWriter.getFileSize() - 1;
            writableValue.append(COMMA)
                    .append(DOUBLE_QUOTE)
                    .append(keyword)
                    .append(DOUBLE_QUOTE)
                    .append(COLON)
                    .append(LEFT_SQUARE_BRACKET)
                    .append(DOUBLE_QUOTE)
                    .append(entityId)
                    .append(DOUBLE_QUOTE)
                    .append(RIGHT_SQUARE_BRACKET);
        } else {
            // one double quote = 1 byte, one colon = 1 byte and one square bracket = 1 byte
            keyWordIndex += keyword.getBytes(StandardCharsets.UTF_8).length + 3;
            writableValue.append(DOUBLE_QUOTE)
                    .append(keyword)
                    .append(DOUBLE_QUOTE)
                    .append(COLON)
                    .append(DOUBLE_QUOTE)
                    .append(entityId)
                    .append(DOUBLE_QUOTE)
                    .append(COMMA);
        }
        try {
            String keywordBuilt = writableValue.toString();
            int result = fileWriter.write(keyWordIndex, keywordBuilt);
            indexManager.updateIndex(keywordBuilt, result);
            return result;
        } catch (IOException e) {
            log.error("Write content failure because IOException", e);
            return 0;
        }
    }
}
