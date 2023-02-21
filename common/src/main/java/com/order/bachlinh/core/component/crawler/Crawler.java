package com.order.bachlinh.core.component.crawler;

import com.order.bachlinh.core.component.crawler.spi.Finder;

public interface Crawler {

    void goTo(String url);

    void closeWindow();

    void quit();

    Finder getFinder();
}
