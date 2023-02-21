package com.order.bachlinh.core.component.client.template.spi;

import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;

public interface RestTemplateFactory {
    RestTemplate create() throws Exception;

    interface Builder {
        Builder pemCertificatePath(String path);

        Builder pemCertificateKeyPath(String path);

        Builder socketTimeOutInMinutes(long value);

        Builder connectionTimeoutInMinutes(long value);

        Builder timeToLiveInMinutes(long value);

        Builder concurrencyPolicy(PoolConcurrencyPolicy value);

        Builder reusePolicy(PoolReusePolicy value);

        Builder cookieSpec(String value);

        Builder cookieStore(CookieStore value);

        RestTemplateFactory build();
    }
}
