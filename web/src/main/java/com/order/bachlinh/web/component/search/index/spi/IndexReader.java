package com.order.bachlinh.web.component.search.index.spi;

public interface IndexReader {
    String read(long position, int keywordSize);
    boolean isExist(String keyword);
}
