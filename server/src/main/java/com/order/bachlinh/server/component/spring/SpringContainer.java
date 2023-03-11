package com.order.bachlinh.server.component.spring;

import com.order.bachlinh.core.configuration.HttpClientConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HttpClientConfiguration.class)
public class SpringContainer {
}
