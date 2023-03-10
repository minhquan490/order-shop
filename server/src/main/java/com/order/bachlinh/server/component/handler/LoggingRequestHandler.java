package com.order.bachlinh.server.component.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.net.http.HttpRequest;

@Log4j2
public final class LoggingRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        handleHttpRequest(msg, ctx);
        super.channelRead(ctx, msg);
    }

    private void handleHttpRequest(Object message, ChannelHandlerContext ctx) {
        String userAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        if (message instanceof HttpRequest request) {
            log.info("Request come from: {}", userAddress);
            log.info(request.headers().map());
        }
    }
}
