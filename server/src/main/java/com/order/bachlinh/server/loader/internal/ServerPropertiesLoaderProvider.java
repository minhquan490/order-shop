package com.order.bachlinh.server.loader.internal;

import com.order.bachlinh.server.loader.spi.ServerPropertiesLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class ServerPropertiesLoaderProvider {

    public static ServerPropertiesLoader propertiesLoader() {
        return new DefaultServerPropertiesLoader();
    }
}
