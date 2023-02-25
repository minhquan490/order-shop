package com.order.bachlinh.web.component.search.index.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexLocator;
import com.order.bachlinh.web.component.search.index.spi.IndexReader;
import com.order.bachlinh.web.component.search.utils.FileReader;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
class DefaultIndexReader implements IndexReader {

    private final IndexLocator locator;
    private final FileReader fileReader;

    DefaultIndexReader(IndexLocator locator, FileReader fileReader) {
        this.locator = locator;
        this.fileReader = fileReader;
    }

    @Override
    public String read(long position, int keywordSize) {
        try {
            String result = fileReader.read(position, keywordSize).split(":")[0];
            result = result
                    .replace("[", "")
                    .replace("]", "");
            return result;
        } catch (IOException e) {
            log.error("Can not read index file because IOException", e);
            return "";
        }
    }

    @Override
    public boolean isExist(String keyword) {
        return locator.locate(keyword) >= 0;
    }
}
