package com.order.bachlinh.web.component.search.index.spi;

public interface IndexContext {
    void addKeyword(String keyword);
    void removeKeyword(String keyword);
    boolean isExist(String keyword);
    String getKeyword(long position);
    void destroy();
}
