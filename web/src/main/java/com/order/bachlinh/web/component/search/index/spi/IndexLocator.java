package com.order.bachlinh.web.component.search.index.spi;

public interface IndexLocator {

    /**
     * Locate the keyword in index context.
     *
     * @param keyword The keyword for find.
     * @return Position of keyword or negative number if not found.
     * */
    long locate(String keyword);

    /**
     * Locate the previous keyword with given keyword.
     *
     * @param keyword The keyword for find.
     * @return Position of previous keyword or negative number if not found.
     * */
    long locatePrevious(String keyword);

    /**
     * Locate the next keyword with given keyword.
     *
     * @param keyword The keyword for find.
     * @return Position of the next keyword or negative number if not found.
     * */
    long locateNext(String keyword);

    long locatePreviousExtract(String keyword);

    long locateNextExtract(String keyword);
    void clear();
}
