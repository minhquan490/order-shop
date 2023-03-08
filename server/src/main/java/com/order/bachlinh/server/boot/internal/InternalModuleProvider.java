package com.order.bachlinh.server.boot.internal;

import com.order.bachlinh.server.boot.spi.ChannelHandlerFactory;
import com.order.bachlinh.server.boot.spi.ChannelInitializer;
import com.order.bachlinh.server.boot.spi.EventLoopGroupFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class InternalModuleProvider {

    public static ChannelHandlerFactory.Builder channelHandlerFactoryBuilder() {
        return SimpleChannelHandlerFactory.builder();
    }

    public static EventLoopGroupFactory.Builder eventLoopGroupFactoryBuilder() {
        return SimpleEventLoopGroupFactory.builder();
    }

    public static ChannelInitializer.Builder channelInitializerBuilder() {
        return SimpleChannelInitializer.builder();
    }
}
