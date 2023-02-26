package com.order.bachlinh.core.component.search.index.internal;

import com.order.bachlinh.core.component.search.utils.FileReader;
import com.order.bachlinh.core.component.search.index.spi.IndexContext;
import com.order.bachlinh.core.component.search.index.spi.IndexLocator;
import com.order.bachlinh.core.component.search.index.spi.IndexReader;
import com.order.bachlinh.core.component.search.index.spi.IndexWriter;
import com.order.bachlinh.core.component.search.utils.FileWriter;

class SimpleIndexContext implements IndexContext {
    private final IndexReader reader;
    private final IndexWriter writer;
    private final IndexLocator locator;

    SimpleIndexContext(IndexLocator locator, FileWriter fileWriter, FileReader fileReader) {
        this.reader = new DefaultIndexReader(locator, fileReader);
        this.writer = new DefaultIndexWriter(locator, fileWriter);
        this.locator = locator;
    }

    @Override
    public void addKeyword(String keyword, long position) {
        locator.setKeywordPosition(position);
        writer.write(keyword);
    }

    @Override
    public boolean isExist(String keyword) {
        return reader.isExist(keyword);
    }

    @Override
    public String getKeyword(long position, int keywordSize) {
        return reader.read(position, keywordSize);
    }

    @Override
    public void destroy() {
        //No effect
    }
}
