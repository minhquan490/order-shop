package com.order.bachlinh.core.component.client.java.spi;

import java.net.http.HttpClient;

public interface HttpClientFactory {
    HttpClient createJavaCoreClient();

    interface Builder extends HttpClient.Builder {

        @Override
        default HttpClient build() {
            throw new UnsupportedOperationException("Unsupported for raw build");
        }

        HttpClientFactory buildFactory();
    }
}
