package com.order.bachlinh.core.component.crawler.internal;

import com.order.bachlinh.core.component.crawler.spi.Closer;
import com.order.bachlinh.core.component.crawler.spi.WebDriverHolder;

class DefaultCloser implements Closer {

    private final WebDriverHolder holder;

    DefaultCloser(WebDriverHolder holder) {
        this.holder = holder;
    }

    @Override
    public void close() {
        holder.getDriver().close();
    }

    @Override
    public void quit() {
        holder.getDriver().quit();
    }
}
