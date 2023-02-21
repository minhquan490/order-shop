package com.order.bachlinh.core.component.crawler.internal;

import com.order.bachlinh.core.component.crawler.spi.FindResult;
import org.openqa.selenium.WebElement;

class DefaultFindResult implements FindResult {

    private final WebElement result;

    DefaultFindResult(WebElement result) {
        this.result = result;
    }

    @Override
    public WebElement getResult() {
        return result;
    }
}
