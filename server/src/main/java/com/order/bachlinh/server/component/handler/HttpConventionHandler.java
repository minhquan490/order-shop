package com.order.bachlinh.server.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.NonNull;

import java.net.SocketAddress;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class HttpConventionHandler extends ChannelInboundHandlerAdapter implements ChannelOutboundHandler {
    private static final ObjectMapper SINGLETON = new ObjectMapper();

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.deregister(promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) {
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse<?> response) {
            HttpHeaders headers = new DefaultHttpHeaders();
            response.headers().map().forEach(headers::set);
            DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.valueOf(response.statusCode()),
                        Unpooled.wrappedBuffer(SINGLETON.writeValueAsString(response.body()).getBytes(StandardCharsets.UTF_8)),
                        headers,
                        headers);
            ctx.write(resp);
            return;
        }
        ctx.write(msg);
    }

    @Override
    public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) throws Exception {
        if (msg instanceof FullHttpRequest req) {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
            req.headers().forEach(entry -> requestBuilder.header(entry.getKey(), entry.getValue()));
            String fullUrl = req.uri();
            requestBuilder.uri(URI.create(fullUrl));
            requestBuilder.method(req.method().name(), HttpRequest.BodyPublishers.ofString(new String(req.content().array(), StandardCharsets.UTF_8)));
            super.channelRead(ctx, requestBuilder.build());
            return;
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public static ObjectMapper getSingleton() {
        return SINGLETON;
    }
}
