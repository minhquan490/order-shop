package com.order.bachlinh.web.component.bean;

import com.order.bachlinh.core.component.search.SearchEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SearchEngineBean {

    @Bean
    SearchEngine searchEngine(ApplicationContext applicationContext) {
        return SearchEngine
                .builder()
                .applicationContext(applicationContext)
                .build();
    }
}
