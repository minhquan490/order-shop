package com.order.bachlinh.server.boot.spi;

import io.netty.incubator.codec.quic.QuicSslContext;

public interface QuicSslContextFactory {
    QuicSslContext manualSslContext(String keyPath, String password, String certPath);
    QuicSslContext defaultSslContext();
}
