package com.order.bachlinh.server.boot.spi;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.incubator.codec.quic.QuicTokenHandler;

public interface ChannelHandlerFactory {
    ChannelHandler create();

    interface Builder {
        Builder sslContext(SslContext sslContext);
        Builder maxIdleTimeout(long millisecond);
        Builder initialMaxData(long maxData);
        Builder maxStreamDataBidirectionalLocal(long maxStreamDataBidirectionalLocal);
        Builder maxStreamDataBidirectionalRemote(long maxStreamDataBidirectionalRemote);
        Builder maxStreamsBidirectional(long maxStreamsBidirectional);
        Builder tokenHandler(QuicTokenHandler tokenHandler);
        Builder channelDecorator(ChannelDecorator<? extends Channel>... decorators);
        Builder maxStreamsUnidirectional(long maxStreamsUnidirectional);
        Builder maxStreamDataUnidirectional(long maxStreamDataUnidirectional);
        ChannelHandlerFactory build();
    }
}
