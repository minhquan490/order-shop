package com.order.bachlinh.web.component.search.index.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexLocator;
import com.order.bachlinh.web.component.search.index.spi.IndexWriter;
import com.order.bachlinh.web.component.search.utils.FileWriter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
class DefaultIndexWriter implements IndexWriter {

    private final IndexLocator locator;
    private final FileWriter fileWriter;

    DefaultIndexWriter(IndexLocator locator, FileWriter fileWriter) {
        this.locator = locator;
        this.fileWriter = fileWriter;
    }

    @Override
    public long write(String keyword) {
        long position = locator.locate(keyword);
        try {
            return fileWriter.write(position, keyword);
        } catch (IOException e) {
            log.error("Write keyword failure because IOException", e);
            return -1;
        }
    }
}
