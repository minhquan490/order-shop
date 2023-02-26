package com.order.bachlinh.core.component.search.index.spi;

public interface IndexReader {
    String read(long position, int keywordSize);
    boolean isExist(String keyword);
}
