package com.order.bachlinh.web.component.search.index.spi;

public interface IndexDestroyer {
    void destroy(String keyword);
    void destroy(int indexOfKeyword);
}
