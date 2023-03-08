package com.order.bachlinh.server.handler.spi;

import io.netty.channel.ChannelHandler;

import java.util.Collection;

public interface ChannelHandlerLookup {

    Collection<ChannelHandler> lookupHandlers(String location);
}
