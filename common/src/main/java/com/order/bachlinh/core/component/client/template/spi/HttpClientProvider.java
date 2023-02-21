package com.order.bachlinh.core.component.client.template.spi;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;

public interface HttpClientProvider {
    HttpClient getHttpClient(HttpClientConnectionManager connectionManager, String cookieSpec, CookieStore cookieStore);
}
