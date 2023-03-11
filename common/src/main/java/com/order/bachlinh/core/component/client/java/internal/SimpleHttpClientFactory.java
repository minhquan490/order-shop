package com.order.bachlinh.core.component.client.java.internal;

import com.order.bachlinh.core.component.client.java.spi.HttpClientFactory;
import com.order.bachlinh.core.component.client.ssl.internal.SslProvider;
import com.order.bachlinh.core.component.client.ssl.spi.SslContextFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executor;

class SimpleHttpClientFactory implements HttpClientFactory {

    private CookieHandler cookieHandler;
    private final Duration connectionTimeout;
    private SSLContext sslContext;
    private final SSLParameters sslParameters;
    private final Executor executor;
    private final HttpClient.Redirect policy;
    private HttpClient.Version version;
    private final int priority;
    private final ProxySelector proxySelector;
    private final Authenticator authenticator;

    SimpleHttpClientFactory(CookieHandler cookieHandler,
                            Duration connectionTimeout,
                            SSLContext sslContext,
                            SSLParameters sslParameters,
                            Executor executor,
                            HttpClient.Redirect policy,
                            HttpClient.Version version,
                            int priority,
                            ProxySelector proxySelector,
                            Authenticator authenticator) {
        this.cookieHandler = cookieHandler;
        this.connectionTimeout = connectionTimeout;
        this.sslContext = sslContext;
        this.sslParameters = sslParameters;
        this.executor = executor;
        this.policy = policy;
        this.version = version;
        this.priority = priority;
        this.proxySelector = proxySelector;
        this.authenticator = authenticator;
    }

    @Override
    public HttpClient createJavaCoreClient() {
        if (sslContext == null) {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            String certPath = "localhost.pem";
            String certKey = "localhost-key.pem";
            SslContextFactory.Builder builder = SslProvider.findSslContextFactoryBuilder();
            builder.pemCertificatePath(classLoader.getResource(certPath).getPath());
            builder.pemPrivateKeyPath(classLoader.getResource(certKey).getPath());
            try {
                sslContext = builder.build().buildContext();
            } catch (Exception e) {
                throw new IllegalStateException("Can not build default ssl context", e);
            }
        }
        if (version == null) {
            version = HttpClient.Version.HTTP_2;
        }
        if (cookieHandler == null) {
            cookieHandler = new CookieManager();
        }
        return HttpClient.newBuilder()
                .authenticator(authenticator)
                .executor(executor)
                .priority(priority)
                .proxy(proxySelector)
                .cookieHandler(cookieHandler)
                .connectTimeout(connectionTimeout)
                .sslContext(sslContext)
                .sslParameters(sslParameters)
                .followRedirects(policy)
                .version(version)
                .build();
    }

    static class SimpleHttpClientFactoryBuilder implements HttpClientFactory.Builder {
        private CookieHandler cookieHandler;
        private Duration connectionTimeout;
        private SSLContext sslContext;
        private SSLParameters sslParameters;
        private Executor executor;
        private HttpClient.Redirect policy;
        private HttpClient.Version version;
        private int priority;
        private ProxySelector proxySelector;
        private Authenticator authenticator;

        @Override
        public HttpClient.Builder cookieHandler(CookieHandler cookieHandler) {
            this.cookieHandler = cookieHandler;
            return this;
        }

        @Override
        public HttpClient.Builder connectTimeout(Duration duration) {
            this.connectionTimeout = duration;
            return this;
        }

        @Override
        public HttpClient.Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        @Override
        public HttpClient.Builder sslParameters(SSLParameters sslParameters) {
            this.sslParameters = sslParameters;
            return this;
        }

        @Override
        public HttpClient.Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        @Override
        public HttpClient.Builder followRedirects(HttpClient.Redirect policy) {
            this.policy = policy;
            return this;
        }

        @Override
        public HttpClient.Builder version(HttpClient.Version version) {
            this.version = version;
            return this;
        }

        @Override
        public HttpClient.Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        @Override
        public HttpClient.Builder proxy(ProxySelector proxySelector) {
            this.proxySelector = proxySelector;
            return this;
        }

        @Override
        public HttpClient.Builder authenticator(Authenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        @Override
        public HttpClientFactory buildFactory() {
            return new SimpleHttpClientFactory(cookieHandler,
                    connectionTimeout,
                    sslContext,
                    sslParameters,
                    executor,
                    policy,
                    version,
                    priority,
                    proxySelector,
                    authenticator);
        }
    }
}
