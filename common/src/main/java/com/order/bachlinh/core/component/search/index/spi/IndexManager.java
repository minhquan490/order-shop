package com.order.bachlinh.core.component.search.index.spi;

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
    String findNextKeywordExtract(String rootKeyword);

    String findNextKeyword(String rootKeyword);

    String findPreviousKeyword(String rootKeyword);

    String findPreviousKeywordExtract(String keyword);

    void createIndex(String keyword, long indexOfKeyword);

    void removeIndex(String keyword);

    void removeIndex(long indexOfKeyword);

    void close();
}
