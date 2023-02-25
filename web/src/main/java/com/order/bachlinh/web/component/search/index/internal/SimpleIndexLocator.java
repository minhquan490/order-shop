package com.order.bachlinh.web.component.search.index.internal;

import com.order.bachlinh.web.component.search.index.spi.AbstractIndexLocator;
import com.order.bachlinh.web.component.search.utils.FileReader;
import lombok.NonNull;

class SimpleIndexLocator extends AbstractIndexLocator {

    SimpleIndexLocator(@NonNull FileReader reader, IndexPositionContainer container) {
        super(reader, container);
    }

    @Override
    public long locate(String keyword) {
        return findKeywordIndex(keyword);
    }

    @Override
    public long locatePrevious(String keyword) {
        return findPreviousIndex(keyword);
    }

    @Override
    public long locateNext(String keyword) {
        return findNextIndex(keyword);
    }

    @Override
    public long locateExtract(String keyword) {
        return fullScanIndexTree(keyword, 0);
    }

    @Override
    public long locatePreviousExtract(String keyword) {
        return fullScanIndexTree(keyword, -1);
    }

    @Override
    public long locateNextExtract(String keyword) {
        return fullScanIndexTree(keyword, 1);
    }
}
