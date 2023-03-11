package com.order.bachlinh.server.boot.internal;

import com.order.bachlinh.server.boot.spi.ChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class SimpleChannelInitializer implements ChannelInitializer {
    private final EventLoopGroup group;
    private final Class<? extends Channel> channelType;
    private final ChannelHandler handler;
    private final String hostName;
    private final int port;
    private final Collection<GenericFutureListener<? extends Future<? super Void>>> listeners;

    @Override
    @SuppressWarnings("unchecked")
    public Channel initialize() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        return bootstrap
                .group(group)
                .channel(channelType)
                .handler(handler)
                .bind(new InetSocketAddress(hostName, port))
                .addListeners(listeners.toArray(new GenericFutureListener[0]))
                .sync()
                .channel();
    }

    static ChannelInitializer.Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    private static class Builder implements ChannelInitializer.Builder {
        private EventLoopGroup group;
        private Class<? extends Channel> channelType;
        private ChannelHandler handler;
        private String hostName;
        private int port;
        private Collection<GenericFutureListener<? extends Future<? super Void>>> listeners;

        @Override
        public ChannelInitializer.Builder group(EventLoopGroup group) {
            this.group = group;
            return this;
        }

        @Override
        public ChannelInitializer.Builder channel(Class<? extends Channel> channelType) {
            this.channelType = channelType;
            return this;
        }

        @Override
        public ChannelInitializer.Builder handler(ChannelHandler handler) {
            this.handler = handler;
            return this;
        }

        @Override
        public ChannelInitializer.Builder host(String hostName) {
            this.hostName = hostName;
            return this;
        }

        @Override
        public ChannelInitializer.Builder port(int port) {
            this.port = port;
            return this;
        }

        @SafeVarargs
        @Override
        public final ChannelInitializer.Builder eventListener(GenericFutureListener<? extends Future<? super Void>>... eventListeners) {
            this.listeners = Stream.of(eventListeners).collect(Collectors.toSet());
            return this;
        }

        @Override
        public ChannelInitializer build() {
            if (listeners == null) {
                listeners = Collections.emptySet();
            }
            return new SimpleChannelInitializer(group, channelType, handler, hostName, port, listeners);
        }
    }
}
