package com.order.bachlinh.web.component.search.index.internal;

import com.order.bachlinh.web.component.search.index.spi.IndexContext;
import com.order.bachlinh.web.component.search.index.spi.IndexLocator;
import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class DefaultIndexManager implements IndexManager {
    private final IndexContext context;
    private final IndexLocator locator;

    @Override
    public long findIndex(String keyword) {
        return locator.locate(keyword);
    }

    @Override
    public boolean isExist(String keyword) {
        return context.isExist(keyword);
    }

    @Override
    public String findNextKeywordExtract(String rootKeyword) {
        long position = locator.locateNextExtract(rootKeyword);
        return context.getKeyword(position);
    }

    @Override
    public String findNextKeyword(String rootKeyword) {
        long position = locator.locateNext(rootKeyword);
        return context.getKeyword(position);
    }

    @Override
    public String findPreviousKeyword(String rootKeyword) {
        long position = locator.locatePrevious(rootKeyword);
        return context.getKeyword(position);
    }

    @Override
    public String findPreviousKeywordExtract(String keyword) {
        long position = locator.locatePreviousExtract(keyword);
        return context.getKeyword(position);
    }

    @Override
    public void updateIndex(String keyword) {
        context.addKeyword(keyword);
    }

    @Override
    public void removeIndex(String keyword) {
        context.removeKeyword(keyword);
    }

    @Override
    public void removeIndex(long indexOfKeyword) {
        String keyword = context.getKeyword(indexOfKeyword);
        context.removeKeyword(keyword);
    }

    @Override
    public void close() {
        context.destroy();
        locator.clear();
    }
}
