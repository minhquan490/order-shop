package com.order.bachlinh.server.component.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;

public final class PathMatchHandler extends ChannelInboundHandlerAdapter {
    private static final String clientUrl = "https://localhost:8080";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest req) {
            switch (req.method().name()) {
                case "GET" -> {
                    String contentType = req.headers().get(HttpHeaderNames.CONTENT_TYPE);
                    if (contentType == null) {

                    }
                    super.channelRead(ctx, msg);
                }
                case "POST" -> {}
                case "PUT" -> {}
                case "DELETE" -> {}
                default -> ctx.fireExceptionCaught(new UnsupportedOperationException("Only support method GET, POST, PUT, DELETE"));
            }
            return;
        }
        super.channelRead(ctx, msg);
    }
}
