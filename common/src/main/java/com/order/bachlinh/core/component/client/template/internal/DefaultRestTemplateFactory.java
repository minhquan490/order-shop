package com.order.bachlinh.core.component.client.template.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.bachlinh.core.component.client.ssl.internal.SslProvider;
import com.order.bachlinh.core.component.client.ssl.spi.SslConnectionSocketFactoryProvider;
import com.order.bachlinh.core.component.client.template.spi.HttpClientProvider;
import com.order.bachlinh.core.component.client.template.spi.PoolingHttpClientConnectionManagerProvider;
import com.order.bachlinh.core.component.client.template.spi.RestTemplate;
import com.order.bachlinh.core.component.client.template.spi.RestTemplateFactory;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.Collections;
import java.util.Map;

class DefaultRestTemplateFactory implements RestTemplateFactory {
    private final Long socketTimeOut;
    private final Long connectionTimeOut;
    private final Long timeToLive;
    private final PoolConcurrencyPolicy concurrencyPolicy;
    private final PoolReusePolicy reusePolicy;
    private final PoolingHttpClientConnectionManagerProvider clientConnectionManagerProvider;
    private final HttpClientProvider httpClientProvider;
    private final String cookieSpec;
    private final CookieStore cookieStore;

    DefaultRestTemplateFactory(String certPath,
                               String keyPath,
                               @Nullable Long socketTimeOut,
                               @Nullable Long connectionTimeOut,
                               @Nullable Long timeToLive,
                               @Nullable PoolConcurrencyPolicy concurrencyPolicy,
                               @Nullable PoolReusePolicy reusePolicy,
                               @Nullable String cookieSpec,
                               @Nullable CookieStore cookieStore) {
        this.socketTimeOut = socketTimeOut;
        this.connectionTimeOut = connectionTimeOut;
        this.timeToLive = timeToLive;
        this.concurrencyPolicy = concurrencyPolicy;
        this.reusePolicy = reusePolicy;
        this.clientConnectionManagerProvider = new PoolingHttpConnectionManagerProvider(certPath, keyPath);
        this.httpClientProvider = new HttpClientProviderImplementer();
        this.cookieSpec = cookieSpec;
        this.cookieStore = cookieStore;
    }

    @Override
    public RestTemplate create() throws Exception {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
        if (isDefault()) {
            poolingHttpClientConnectionManager = clientConnectionManagerProvider.setup();
        } else {
            poolingHttpClientConnectionManager = clientConnectionManagerProvider.setup(socketTimeOut, connectionTimeOut, timeToLive, concurrencyPolicy, reusePolicy);
        }
        HttpClient client = httpClientProvider.getHttpClient(poolingHttpClientConnectionManager, cookieSpec, cookieStore);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        return new RestTemplateImpl(requestFactory);
    }

    private boolean isDefault() {
        return socketTimeOut == null ||
                connectionTimeOut == null ||
                timeToLive == null ||
                concurrencyPolicy == null ||
                reusePolicy == null;
    }


    private record PoolingHttpConnectionManagerProvider(String certPath,
                                                        String keyPath) implements PoolingHttpClientConnectionManagerProvider {
        @Override
        public PoolingHttpClientConnectionManager setup(long socketTimeOutInMinutes, long connectionTimeoutInMinutes, long timeToLiveInMinutes, PoolConcurrencyPolicy concurrencyPolicy, PoolReusePolicy reusePolicy) throws Exception {
            SslConnectionSocketFactoryProvider provider = SslProvider.buildProvider();
            SSLConnectionSocketFactory socketFactory = provider.getSslConnectionSocketFactory(certPath, keyPath);
            return PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(socketFactory)
                    .setDefaultSocketConfig(SocketConfig.custom()
                            .setSoTimeout(Timeout.ofMinutes(socketTimeOutInMinutes))
                            .build())
                    .setPoolConcurrencyPolicy(concurrencyPolicy)
                    .setConnPoolPolicy(reusePolicy)
                    .setDefaultConnectionConfig(ConnectionConfig.custom()
                            .setSocketTimeout(Timeout.ofMinutes(socketTimeOutInMinutes))
                            .setConnectTimeout(Timeout.ofMinutes(connectionTimeoutInMinutes))
                            .setTimeToLive(TimeValue.ofMinutes(timeToLiveInMinutes))
                            .build())
                    .build();
        }

        @Override
        public PoolingHttpClientConnectionManager setup() throws Exception {
            return setup(1, 1, 10, PoolConcurrencyPolicy.STRICT, PoolReusePolicy.LIFO);
        }
    }

    private record HttpClientProviderImplementer() implements HttpClientProvider {

        @Override
        public HttpClient getHttpClient(HttpClientConnectionManager connectionManager, String cookieSpec, CookieStore cookieStore) {
            if (cookieSpec == null) {
                cookieSpec = StandardCookieSpec.STRICT;
            }
            if (cookieStore == null) {
                cookieStore = new BasicCookieStore();
            }
            return HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setCookieSpec(cookieSpec)
                            .build())
                    .setDefaultCookieStore(cookieStore)
                    .build();
        }
    }

    private static class RestTemplateImpl implements RestTemplate {
        private final org.springframework.web.client.RestTemplate internalTemplate;
        private final ObjectMapper jsonConverter;

        private RestTemplateImpl(ClientHttpRequestFactory requestFactory) {
            this.internalTemplate = new org.springframework.web.client.RestTemplate(requestFactory);
            this.jsonConverter = new ObjectMapper();
        }

        @Override
        public JsonNode get(String url, Object body, MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException {
            return request(url, HttpMethod.GET, body, headers, uriVariables);
        }

        @Override
        public JsonNode put(String url, Object body, MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException {
            return request(url, HttpMethod.PUT, body, headers, uriVariables);
        }

        @Override
        public JsonNode post(String url, Object body, MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException {
            return request(url, HttpMethod.POST, body, headers, uriVariables);
        }

        @Override
        public JsonNode delete(String url, Object body, MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException {
            return request(url, HttpMethod.DELETE, body, headers, uriVariables);
        }

        private JsonNode request(String url, HttpMethod method, Object body, MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException {
            HttpHeaders httpHeaders = new HttpHeaders(headers == null ? new MultiValueMapAdapter<>(Collections.emptyMap()) : headers);
            HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<String> response = internalTemplate.exchange(url, method, httpEntity, String.class, uriVariables);
            return jsonConverter.readTree(response.getBody());
        }
    }
}
