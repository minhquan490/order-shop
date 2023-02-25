package com.order.bachlinh.web.component.search.index.spi;

public interface IndexContext {
    void addKeyword(String keyword, long position);
    boolean isExist(String keyword);
    String getKeyword(long position, int keywordSize);
    void destroy();
}
