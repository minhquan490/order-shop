package com.order.bachlinh.server.boot.internal;

import com.order.bachlinh.server.boot.spi.ChannelDecorator;
import com.order.bachlinh.server.boot.spi.ChannelHandlerFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.incubator.codec.http3.Http3;
import io.netty.incubator.codec.http3.Http3ServerConnectionHandler;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicSslContext;
import io.netty.incubator.codec.quic.QuicStreamChannel;
import io.netty.incubator.codec.quic.QuicTokenHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SimpleChannelHandlerFactory implements ChannelHandlerFactory {
    private SslContext sslContext;
    private final long idleTimeout;
    private final long initialMaxData;
    private final long initialMaxStreamDataBidirectionalLocal;
    private final long initialMaxStreamDataBidirectionalRemote;
    private final long initialMaxStreamsBidirectional;
    private final QuicTokenHandler tokenHandler;
    private final ChannelHandler handler;
    private final long initialMaxStreamsUnidirectional;
    private final long initialMaxStreamDataUnidirectional;

    @Override
    public ChannelHandler create() {
        return Http3.newQuicServerCodecBuilder()
                .sslContext((QuicSslContext) sslContext)
                .maxIdleTimeout(idleTimeout, TimeUnit.MICROSECONDS)
                .initialMaxData(initialMaxData)
                .initialMaxStreamDataBidirectionalLocal(initialMaxStreamDataBidirectionalLocal)
                .initialMaxStreamDataBidirectionalRemote(initialMaxStreamDataBidirectionalRemote)
                .initialMaxStreamsBidirectional(initialMaxStreamsBidirectional)
                .initialMaxStreamsUnidirectional(initialMaxStreamsUnidirectional)
                .initialMaxStreamDataUnidirectional(initialMaxStreamDataUnidirectional)
                .tokenHandler(tokenHandler)
                .handler(handler)
                .build();
    }

    static ChannelHandlerFactory.Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    private static class Builder implements ChannelHandlerFactory.Builder {
        private SslContext sslContext;
        private long maxIdleTimeout;
        private long initialMaxData;
        private long initialMaxStreamDataBidirectionalLocal;
        private long initialMaxStreamDataBidirectionalRemote;
        private long initialMaxStreamsBidirectional;
        private long initialMaxStreamsUnidirectional;
        private long initialMaxStreamDataUnidirectional;
        private QuicTokenHandler tokenHandler;
        private final Collection<ChannelDecorator<? extends Channel>> channelDecorators = new HashSet<>();

        @Override
        public ChannelHandlerFactory.Builder sslContext(SslContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder maxIdleTimeout(long millisecond) {
            this.maxIdleTimeout = millisecond;
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder initialMaxData(long maxData) {
            this.initialMaxData = maxData;
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder maxStreamDataBidirectionalLocal(long maxStreamDataBidirectionalLocal) {
            this.initialMaxStreamDataBidirectionalLocal = maxStreamDataBidirectionalLocal;
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder maxStreamDataBidirectionalRemote(long maxStreamDataBidirectionalRemote) {
            this.initialMaxStreamDataBidirectionalRemote = maxStreamDataBidirectionalRemote;
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder maxStreamsBidirectional(long maxStreamsBidirectional) {
            this.initialMaxStreamsBidirectional = maxStreamsBidirectional;
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder tokenHandler(QuicTokenHandler tokenHandler) {
            this.tokenHandler = tokenHandler;
            return this;
        }

        @SafeVarargs
        @Override
        public final ChannelHandlerFactory.Builder channelDecorator(ChannelDecorator<? extends Channel>... decorators) {
            this.channelDecorators.addAll(Stream.of(decorators).collect(Collectors.toSet()));
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder maxStreamsUnidirectional(long maxStreamsUnidirectional) {
            this.initialMaxStreamsUnidirectional = maxStreamsUnidirectional;
            return this;
        }

        @Override
        public ChannelHandlerFactory.Builder maxStreamDataUnidirectional(long maxStreamDataUnidirectional) {
            this.initialMaxStreamDataUnidirectional = maxStreamDataUnidirectional;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public ChannelHandlerFactory build() {
            Collection<ChannelDecorator<QuicChannel>> quicChannelDecorators = channelDecorators.stream()
                    .filter(decorator -> findParameterizedType(decorator, QuicChannel.class) != null)
                    .map(decorator -> (ChannelDecorator<QuicChannel>) decorator)
                    .collect(Collectors.toSet());
            Collection<ChannelDecorator<QuicStreamChannel>> quicStreamChannelDecorators = channelDecorators.stream()
                    .filter(decorator -> findParameterizedType(decorator, QuicStreamChannel.class) != null)
                    .map(decorator -> (ChannelDecorator<QuicStreamChannel>) decorator)
                    .collect(Collectors.toSet());
            return new SimpleChannelHandlerFactory(sslContext,
                    maxIdleTimeout,
                    initialMaxData,
                    initialMaxStreamDataBidirectionalLocal,
                    initialMaxStreamDataBidirectionalRemote,
                    initialMaxStreamsBidirectional,
                    tokenHandler,
                    createChannelHandler(quicChannelDecorators, quicStreamChannelDecorators),
                    initialMaxStreamsUnidirectional,
                    initialMaxStreamDataUnidirectional);
        }

        private ChannelHandler createChannelHandler(Collection<ChannelDecorator<QuicChannel>> quicChannelDecorators, Collection<ChannelDecorator<QuicStreamChannel>> quicStreamChannelDecorators) {
            return new ChannelInitializer<QuicChannel>() {

                @Override
                protected void initChannel(QuicChannel ch) {
                    quicChannelDecorators.forEach(decorator -> decorator.decorate(ch));
                    ch.pipeline().addLast(new Http3ServerConnectionHandler(new ChannelInitializer<QuicStreamChannel>() {
                        @Override
                        protected void initChannel(QuicStreamChannel ch) {
                            quicStreamChannelDecorators.forEach(decorator -> decorator.decorate(ch));
                        }
                    }));
                }
            };
        }

        @SuppressWarnings("unchecked")
        private <T extends Channel> Class<T> findParameterizedType(ChannelDecorator<? extends Channel> target, Class<T> type) {
            Class<T> targetType = (Class<T>) ((ParameterizedType) target.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (type.isAssignableFrom(targetType)) {
                return targetType;
            }
            return null;
        }
    }
}
