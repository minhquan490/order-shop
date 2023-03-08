package com.order.bachlinh.server.handler.spi;

import com.order.bachlinh.server.boot.spi.ChannelDecorator;
import io.netty.channel.Channel;

import java.util.Collection;

public interface ChannelDecoratorProvider {
    Collection<ChannelDecorator<? extends Channel>> getChannelDecorator();
}
