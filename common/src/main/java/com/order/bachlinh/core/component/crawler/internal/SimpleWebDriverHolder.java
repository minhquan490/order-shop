package com.order.bachlinh.core.component.crawler.internal;

import com.order.bachlinh.core.component.crawler.spi.AbstractWebDriverHolder;
import org.openqa.selenium.WebDriver;

class SimpleWebDriverHolder extends AbstractWebDriverHolder {
    public SimpleWebDriverHolder(WebDriver driver) {
        super(driver);
    }
}
