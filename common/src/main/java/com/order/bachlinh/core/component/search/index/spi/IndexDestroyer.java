package com.order.bachlinh.core.component.search.index.spi;

public interface IndexDestroyer {
    void destroy(String keyword);
    void destroy(int indexOfKeyword);
}
