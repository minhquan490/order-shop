package com.order.bachlinh.core.component.crawler.internal;

import com.order.bachlinh.core.component.crawler.spi.Navigator;
import com.order.bachlinh.core.component.crawler.spi.WebDriverHolder;

class DefaultNavigator implements Navigator {

    private final WebDriverHolder holder;

    DefaultNavigator(WebDriverHolder holder) {
        this.holder = holder;
    }

    @Override
    public void navigateTo(String url) {
        holder.getDriver().get(url);
    }
}
