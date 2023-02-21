package com.order.bachlinh.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.order.bachlinh.core.component.crawler.Crawler;
import com.order.bachlinh.core.component.crawler.internal.CrawlerProvider;

/**
 * Configuration for crawler to collect data in world wide web
 * */
@Configuration
class CrawlerConfiguration {

    @Value("${shop.web.driver.path}")
    private String webDriverExecutablePath;

    @Bean
    @Lazy
    Crawler crawler() {
        return CrawlerProvider.usePhantomJS(webDriverExecutablePath);
    }
}
