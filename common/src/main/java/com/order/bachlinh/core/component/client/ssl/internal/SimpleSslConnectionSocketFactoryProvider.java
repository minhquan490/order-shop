package com.order.bachlinh.core.component.client.ssl.internal;

import com.order.bachlinh.core.component.client.ssl.spi.SslConnectionSocketFactoryProvider;
import com.order.bachlinh.core.component.client.ssl.spi.SslContextFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;

import javax.net.ssl.SSLContext;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SimpleSslConnectionSocketFactoryProvider implements SslConnectionSocketFactoryProvider {

    @Override
    public SSLConnectionSocketFactory getSslConnectionSocketFactory(String certPath, String keyPath) throws Exception {
        SslContextFactory.Builder builder = SslProvider.findSslContextFactoryBuilder();
        builder.pemCertificatePath(certPath);
        builder.pemPrivateKeyPath(keyPath);
        SSLContext sslContext = builder.build().buildContext();
        return SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .setTlsVersions(sslContext.getProtocol())
                .build();
    }
}
