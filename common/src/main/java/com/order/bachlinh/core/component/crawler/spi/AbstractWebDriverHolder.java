package com.order.bachlinh.core.component.crawler.spi;

import org.openqa.selenium.WebDriver;

public abstract class AbstractWebDriverHolder implements WebDriverHolder {
    private final WebDriver webDriver;

    protected AbstractWebDriverHolder(WebDriver driver) {
        this.webDriver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return webDriver;
    }
}
