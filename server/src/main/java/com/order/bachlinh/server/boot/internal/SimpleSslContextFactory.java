package com.order.bachlinh.server.boot.internal;

import com.order.bachlinh.server.boot.spi.QuicSslContextFactory;
import io.netty.incubator.codec.http3.Http3;
import io.netty.incubator.codec.quic.QuicSslContext;
import io.netty.incubator.codec.quic.QuicSslContextBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class SimpleSslContextFactory implements QuicSslContextFactory {
    @Override
    public QuicSslContext manualSslContext(String keyPath, String password, String certPath) {
        return QuicSslContextBuilder
                .forServer(new File(keyPath), password, new File(certPath))
                .applicationProtocols(Http3.supportedApplicationProtocols())
                .build();
    }

    @Override
    public QuicSslContext defaultSslContext() {
        String rootPath = new File("").getAbsolutePath();
        String certName = "localhost.pem";
        String keyName = "localhost-key.pem";
        String certPath = String.join("/", rootPath, "src", "main", "resources", certName);
        String keyPath = String.join("/", rootPath, "src", "main", "resources", keyName);
        String password = "";
        return manualSslContext(keyPath, password, certPath);
    }
}