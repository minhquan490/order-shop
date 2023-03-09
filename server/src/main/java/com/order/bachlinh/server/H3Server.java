package com.order.bachlinh.server;

import com.order.bachlinh.server.boot.internal.InternalModuleProvider;
import com.order.bachlinh.server.boot.spi.ChannelDecorator;
import com.order.bachlinh.server.boot.spi.ChannelHandlerFactory;
import com.order.bachlinh.server.boot.spi.ChannelInitializer;
import com.order.bachlinh.server.boot.spi.EventLoopGroupFactory;
import com.order.bachlinh.server.boot.spi.QuicSslContextFactory;
import com.order.bachlinh.server.handler.internal.InternalHandlerProvider;
import com.order.bachlinh.server.handler.spi.ChannelDecoratorProvider;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.incubator.codec.quic.QuicTokenHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.Builder;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public final class H3Server {
    private final long idleTimeOut;
    private final long maxData;
    private final long maxStreamDataLocal;
    private final long maxStreamDataRemote;
    private final long maxStream;
    private final long maxStreamsUnidirectional;
    private final long maxStreamDataUnidirectional;
    private final int numberOfThread;
    private final int port;
    private final String host;
    private final String handlerPackage;
    private final String certPath;
    private final String keyPath;
    private final String password;
    private final ChannelInitializer channelInitializer;
    private final QuicTokenHandler tokenHandler;
    private Channel channel;

    @Builder
    private H3Server(long idleTimeOut,
                     long maxData,
                     long maxStreamDataLocal,
                     long maxStreamDataRemote,
                     long maxStream,
                     long maxStreamsUnidirectional,
                     long maxStreamDataUnidirectional,
                     int port,
                     int numberOfThread,
                     String host,
                     String handlerPackage,
                     String certPath,
                     String keyPath,
                     String password,
                     QuicTokenHandler tokenHandler) {
        this.idleTimeOut = idleTimeOut;
        this.maxData = maxData;
        this.maxStreamDataLocal = maxStreamDataLocal;
        this.maxStreamDataRemote = maxStreamDataRemote;
        this.maxStream = maxStream;
        this.numberOfThread = numberOfThread;
        this.port = port;
        this.host = host;
        this.handlerPackage = handlerPackage;
        this.certPath = certPath;
        this.keyPath = keyPath;
        this.password = password;
        this.channelInitializer = buildChannelInitializer();
        this.tokenHandler = tokenHandler;
        this.maxStreamsUnidirectional = maxStreamsUnidirectional;
        this.maxStreamDataUnidirectional = maxStreamDataUnidirectional;
    }

    public void run() throws InterruptedException {
        if (channel == null) {
            channel = channelInitializer.initialize();
            channel.closeFuture().sync();
        }
    }

    public void close() {
        channel.close();
    }

    private EventLoopGroupFactory buildEventLoopGroupFactory() {
        EventLoopGroupFactory.Builder builder = InternalModuleProvider.eventLoopGroupFactoryBuilder();
        ThreadFactory threadFactory = new DefaultThreadFactory("netty-h3-server");
        Executor executor = new UnorderedThreadPoolEventExecutor(numberOfThread, threadFactory);
        return builder.numberOfThread(numberOfThread)
                .threadFactory(threadFactory)
                .executor(executor)
                .build();
    }

    @SuppressWarnings("unchecked")
    private ChannelHandlerFactory buildChannelHandlerFactory() {
        ChannelDecoratorProvider provider = InternalHandlerProvider.channelDecoratorProvider(handlerPackage);
        ChannelHandlerFactory.Builder builder = InternalModuleProvider.channelHandlerFactoryBuilder();
        QuicSslContextFactory sslContextFactory = InternalModuleProvider.sslContextFactory();
        return builder.channelDecorator(provider.getChannelDecorator().toArray(new ChannelDecorator[0]))
                .sslContext(sslContextFactory.manualSslContext(keyPath, password, certPath))
                .maxIdleTimeout(idleTimeOut)
                .initialMaxData(maxData)
                .maxStreamDataBidirectionalLocal(maxStreamDataLocal)
                .maxStreamDataBidirectionalRemote(maxStreamDataRemote)
                .maxStreamsBidirectional(maxStream)
                .tokenHandler(tokenHandler)
                .maxStreamsUnidirectional(maxStreamsUnidirectional)
                .maxStreamDataUnidirectional(maxStreamDataUnidirectional)
                .build();
    }

    private ChannelInitializer buildChannelInitializer() {
        EventLoopGroupFactory eventLoopGroupFactory = buildEventLoopGroupFactory();
        ChannelHandlerFactory channelHandlerFactory = buildChannelHandlerFactory();
        ChannelInitializer.Builder builder = InternalModuleProvider.channelInitializerBuilder();
        return builder.channel(NioDatagramChannel.class)
                .group(eventLoopGroupFactory.buildNioEventLoopGroup())
                .handler(channelHandlerFactory.create())
                .host(host)
                .port(port)
                .build();
    }
}
