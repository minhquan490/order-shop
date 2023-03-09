package com.order.bachlinh.server.handler.internal;

import com.order.bachlinh.server.handler.spi.ChannelDecoratorProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class InternalHandlerProvider {
    public static ChannelDecoratorProvider channelDecoratorProvider(String handlerPackage) {
        return new InternalHandler(handlerPackage);
    }
}
