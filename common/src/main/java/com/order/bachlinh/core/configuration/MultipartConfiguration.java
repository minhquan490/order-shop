package com.order.bachlinh.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@Lazy
class MultipartConfiguration {

    @Bean(name = "multipartResolver")
    @Lazy
    MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
