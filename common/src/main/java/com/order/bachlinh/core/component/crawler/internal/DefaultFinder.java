package com.order.bachlinh.core.component.crawler.internal;

import com.order.bachlinh.core.component.crawler.spi.FindResult;
import com.order.bachlinh.core.component.crawler.spi.Finder;
import com.order.bachlinh.core.component.crawler.spi.WebDriverHolder;
import org.openqa.selenium.By;

import java.util.List;

class DefaultFinder implements Finder {

    private final WebDriverHolder holder;

    DefaultFinder(WebDriverHolder holder) {
        this.holder = holder;
    }

    @Override
    public FindResult findElementById(String id) {
        return new DefaultFindResult(holder.getDriver().findElement(By.id(id)));
    }

    @Override
    public FindResult findElementByTagName(String tagName) {
        return new DefaultFindResult(holder.getDriver().findElement(By.tagName(tagName)));
    }

    @Override
    public FindResult findElementByXPath(String xpath) {
        return new DefaultFindResult(holder.getDriver().findElement(By.xpath(xpath)));
    }

    @Override
    public FindResult findElementByClassName(String className) {
        return new DefaultFindResult(holder.getDriver().findElement(By.className(className)));
    }

    @Override
    public FindResult findElementByCssSelector(String cssSelector) {
        return new DefaultFindResult(holder.getDriver().findElement(By.cssSelector(cssSelector)));
    }

    @Override
    public List<FindResult> findElementsById(String id) {
        return holder.getDriver()
                .findElements(By.id(id))
                .stream()
                .map(DefaultFindResult::new)
                .map(FindResult.class::cast)
                .toList();
    }

    @Override
    public List<FindResult> findElementsTagName(String tagName) {
        return holder.getDriver()
                .findElements(By.tagName(tagName))
                .stream()
                .map(DefaultFindResult::new)
                .map(FindResult.class::cast)
                .toList();
    }

    @Override
    public List<FindResult> findElementsByXPath(String xpath) {
        return holder.getDriver()
                .findElements(By.xpath(xpath))
                .stream()
                .map(DefaultFindResult::new)
                .map(FindResult.class::cast)
                .toList();
    }

    @Override
    public List<FindResult> findElementsByClassName(String className) {
        return holder.getDriver()
                .findElements(By.className(className))
                .stream()
                .map(DefaultFindResult::new)
                .map(FindResult.class::cast)
                .toList();
    }

    @Override
    public List<FindResult> findElementsByCssSelector(String cssSelector) {
        return holder.getDriver()
                .findElements(By.cssSelector(cssSelector))
                .stream()
                .map(DefaultFindResult::new)
                .map(FindResult.class::cast)
                .toList();
    }

    @Override
    public String getTitle() {
        return holder.getDriver().getTitle();
    }
}
