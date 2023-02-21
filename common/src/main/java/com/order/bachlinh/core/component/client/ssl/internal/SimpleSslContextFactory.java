package com.order.bachlinh.core.component.client.ssl.internal;

import com.order.bachlinh.core.component.client.ssl.spi.SslContextFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.boot.web.server.CertificateFileSslStoreProvider;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.SslStoreProvider;

import javax.net.ssl.SSLContext;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SimpleSslContextFactory implements SslContextFactory {

    private final String certPath;
    private final String keyPath;

    @Override
    public SSLContext buildContext() throws Exception {
        Ssl ssl = buildSsl(certPath, keyPath);
        SslStoreProvider provider = CertificateFileSslStoreProvider.from(ssl);
        return SSLContexts.custom()
                .loadTrustMaterial(provider.getTrustStore(), new TrustSelfSignedStrategy())
                .setProtocol(ssl.getProtocol())
                .build();
    }

    private Ssl buildSsl(String certPath, String keyPath) {
        Ssl ssl = new Ssl();
        ssl.setCertificate(certPath);
        ssl.setCertificatePrivateKey(keyPath);
        ssl.setEnabled(true);
        ssl.setKeyAlias("rest-template");
        ssl.setProtocol("TLSv1.3");
        return ssl;
    }
}
