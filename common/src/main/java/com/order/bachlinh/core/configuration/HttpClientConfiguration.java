package com.order.bachlinh.core.configuration;

import com.order.bachlinh.core.component.client.template.internal.RestTemplateFactoryBuilderProvider;
import com.order.bachlinh.core.component.client.template.spi.RestTemplate;
import com.order.bachlinh.core.component.client.template.spi.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HttpClientConfiguration {
    private final String certPath;
    private final String keyPath;

    @Autowired
    HttpClientConfiguration(@Value("${server.ssl.certificate}") String certPath, @Value("${server.ssl.certificate-private-key}") String keyPath) {
        this.certPath = certPath;
        this.keyPath = keyPath;
    }

    @Bean
    RestTemplate restTemplate() throws Exception {
        RestTemplateFactory.Builder builder = RestTemplateFactoryBuilderProvider.getFactoryBuilder();
        return builder.pemCertificatePath(certPath)
                .pemCertificateKeyPath(keyPath)
                .build()
                .create();
    }
}
