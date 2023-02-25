package com.order.bachlinh.web.component.search.index.spi;

public interface IndexManager {
    long findIndex(String keyword);

    boolean isExist(String keyword);

    /**
     * Return a next key word which was inserted after root key word inserted
     * without sorting.
     *
     * @param rootKeyword root key for find.
     * @return next keyword before sort (sorted with position).
     * */
    long findNextKeywordExtract(String rootKeyword);

    long findNextKeyword(String rootKeyword);

    long findPreviousKeyword(String rootKeyword);

    long findPreviousKeywordExtract(String keyword);

    void updateIndex(String keyword, long position);

    void close();
}
