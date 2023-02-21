package com.order.bachlinh.core.component.crawler.spi;

import java.util.List;

public interface Finder {

    FindResult findElementById(String id);

    FindResult findElementByTagName(String tagName);

    FindResult findElementByXPath(String xpath);

    FindResult findElementByClassName(String className);

    FindResult findElementByCssSelector(String cssSelector);

    List<FindResult> findElementsById(String id);

    List<FindResult> findElementsTagName(String tagName);

    List<FindResult> findElementsByXPath(String xpath);

    List<FindResult> findElementsByClassName(String className);

    List<FindResult> findElementsByCssSelector(String cssSelector);

    String getTitle();
}
