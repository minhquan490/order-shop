package com.order.bachlinh.web.component.search.index.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexContext;
import com.order.bachlinh.web.component.search.index.spi.IndexLocator;
import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import com.order.bachlinh.web.component.search.utils.FileReader;
import com.order.bachlinh.web.component.search.utils.FileWriter;

class DefaultIndexManager implements IndexManager {
    private final IndexContext context;
    private final IndexLocator locator;

    DefaultIndexManager(IndexLocator locator, FileWriter fileWriter, FileReader fileReader) {
        this.context = new SimpleIndexContext(locator, fileWriter, fileReader);
        this.locator = locator;
    }

    @Override
    public long findIndex(String keyword) {
        return locator.locate(keyword);
    }

    @Override
    public boolean isExist(String keyword) {
        return context.isExist(keyword);
    }

    @Override
    public long findNextKeywordExtract(String rootKeyword) {
        return locator.locateNextExtract(rootKeyword);
    }

    @Override
    public long findNextKeyword(String rootKeyword) {
        return locator.locateNext(rootKeyword);
    }

    @Override
    public long findPreviousKeyword(String rootKeyword) {
        return locator.locatePrevious(rootKeyword);
    }

    @Override
    public long findPreviousKeywordExtract(String keyword) {
        return locator.locatePreviousExtract(keyword);
    }

    @Override
    public void updateIndex(String keyword, long position) {
        context.addKeyword(keyword, position);
    }

    @Override
    public void close() {
        context.destroy();
        locator.clear();
    }
}
