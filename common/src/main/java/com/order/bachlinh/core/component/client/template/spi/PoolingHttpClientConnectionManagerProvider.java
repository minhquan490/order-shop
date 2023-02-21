package com.order.bachlinh.core.component.client.template.spi;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;

public interface PoolingHttpClientConnectionManagerProvider {
    PoolingHttpClientConnectionManager setup(long socketTimeOutInMinutes,
                                             long connectionTimeoutInMinutes,
                                             long timeToLiveInMinutes,
                                             PoolConcurrencyPolicy concurrencyPolicy,
                                             PoolReusePolicy reusePolicy) throws Exception;

    PoolingHttpClientConnectionManager setup() throws Exception;
}
