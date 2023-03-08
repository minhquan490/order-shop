package com.order.bachlinh.server.boot.spi;


import io.netty.channel.Channel;

@FunctionalInterface
public interface ChannelDecorator<T extends Channel> {
    void decorate(T channel);
}
