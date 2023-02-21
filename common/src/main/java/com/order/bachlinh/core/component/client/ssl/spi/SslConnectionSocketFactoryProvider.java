package com.order.bachlinh.core.component.client.ssl.spi;

import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;

public interface SslConnectionSocketFactoryProvider {
    SSLConnectionSocketFactory getSslConnectionSocketFactory(String certPath, String keyPath) throws Exception;
}
