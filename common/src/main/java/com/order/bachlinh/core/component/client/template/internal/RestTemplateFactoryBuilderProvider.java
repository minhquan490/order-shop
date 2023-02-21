package com.order.bachlinh.core.component.client.template.internal;

import com.order.bachlinh.core.component.client.template.spi.RestTemplateFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public final class RestTemplateFactoryBuilderProvider {

    public static RestTemplateFactory.Builder getFactoryBuilder() {
        return new RestTemplateFactoryBuilderImplementer();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class RestTemplateFactoryBuilderImplementer implements RestTemplateFactory.Builder {
        private String certPath;
        private String keyPath;
        private long socketTimeOut;
        private long connectionTimeOut;
        private long timeToLive;
        private PoolConcurrencyPolicy concurrencyPolicy;
        private PoolReusePolicy reusePolicy;
        private String cookieSpec;
        private CookieStore cookieStore;

        @Override
        public RestTemplateFactory.Builder pemCertificatePath(String path) {
            this.certPath = path;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder pemCertificateKeyPath(String path) {
            this.keyPath = path;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder socketTimeOutInMinutes(long value) {
            this.socketTimeOut = value;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder connectionTimeoutInMinutes(long value) {
            this.connectionTimeOut = value;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder timeToLiveInMinutes(long value) {
            this.timeToLive = value;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder concurrencyPolicy(PoolConcurrencyPolicy value) {
            this.concurrencyPolicy = value;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder reusePolicy(PoolReusePolicy value) {
            this.reusePolicy = value;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder cookieSpec(String value) {
            this.cookieSpec = value;
            return this;
        }

        @Override
        public RestTemplateFactory.Builder cookieStore(CookieStore value) {
            this.cookieStore = value;
            return this;
        }

        @Override
        public RestTemplateFactory build() {
            return new DefaultRestTemplateFactory(certPath,
                    keyPath,
                    socketTimeOut,
                    connectionTimeOut,
                    timeToLive,
                    concurrencyPolicy,
                    reusePolicy,
                    cookieSpec,
                    cookieStore);
        }
    }
}
