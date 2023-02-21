package com.order.bachlinh.core.component.client.ssl.spi;

import javax.net.ssl.SSLContext;

public interface SslContextFactory {

    SSLContext buildContext() throws Exception;

    interface Builder {
        Builder pemCertificatePath(String path);

        Builder pemPrivateKeyPath(String path);

        SslContextFactory build();
    }
}
