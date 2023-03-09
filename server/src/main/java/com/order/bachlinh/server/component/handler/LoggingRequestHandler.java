package com.order.bachlinh.server.component.handler;

import com.order.bachlinh.server.handler.annotation.QuicStreamChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.incubator.codec.http3.Http3DataFrame;
import io.netty.incubator.codec.http3.Http3Headers;
import io.netty.incubator.codec.http3.Http3HeadersFrame;
import io.netty.incubator.codec.http3.Http3RequestStreamInboundHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Log4j2
@QuicStreamChannel(name = "loggingRequestHandler")
@Order(value = -10)
public class LoggingRequestHandler extends Http3RequestStreamInboundHandler {
    
    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3HeadersFrame frame, boolean isLast) throws Exception {
        Http3Headers headers = frame.headers();
        Iterator<Map.Entry<CharSequence, CharSequence>> headerIterator = headers.iterator();
        Map<CharSequence, CharSequence> headerMap = new HashMap<>();
        while (headerIterator.hasNext()) {
            Map.Entry<CharSequence, CharSequence> header = headerIterator.next();
            headerMap.put(header.getKey(), header.getValue());
        }
        log.info("Header of incoming request");
        log.info(headerMap);
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3DataFrame frame, boolean isLast) throws Exception {
        // ignore
    }
}
