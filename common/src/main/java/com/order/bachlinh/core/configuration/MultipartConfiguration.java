package com.order.bachlinh.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
class MultipartConfiguration {

    @Bean(name = "multipartResolver")
    MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
