package com.order.bachlinh.core.component.client.ssl.internal;

import com.order.bachlinh.core.component.client.ssl.spi.SslConnectionSocketFactoryProvider;
import com.order.bachlinh.core.component.client.ssl.spi.SslContextFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class SslProvider {

    static SslContextFactory.Builder findSslContextFactoryBuilder() {
        return new SslContextFactoryBuilderImplementer();
    }

    public static SslConnectionSocketFactoryProvider buildProvider() {
        return new SimpleSslConnectionSocketFactoryProvider();
    }

    private static class SslContextFactoryBuilderImplementer implements SslContextFactory.Builder {
        private String certPath;
        private String keyPath;

        @Override
        public SslContextFactory.Builder pemCertificatePath(String path) {
            this.certPath = path;
            return this;
        }

        @Override
        public SslContextFactory.Builder pemPrivateKeyPath(String path) {
            this.keyPath = path;
            return this;
        }

        @Override
        public SslContextFactory build() {
            return new SimpleSslContextFactory(certPath, keyPath);
        }
    }
}
