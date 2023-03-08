package com.order.bachlinh.server.boot.spi;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface ChannelInitializer {
    Channel initialize() throws InterruptedException;

    interface Builder {
        Builder group(EventLoopGroup group);
        Builder channel(Class<? extends Channel> channelType);
        Builder handler(ChannelHandler handler);
        Builder host(String hostName);
        Builder port(int port);
        Builder eventListener(GenericFutureListener<? extends Future<? super Void>>... eventListeners);
        ChannelInitializer build();
    }
}
