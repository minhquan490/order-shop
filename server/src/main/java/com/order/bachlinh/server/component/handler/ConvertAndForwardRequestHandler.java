package com.order.bachlinh.server.component.handler;

import com.order.bachlinh.core.component.client.template.spi.RestTemplate;
import com.order.bachlinh.server.component.converter.spi.Http3FrameRequestConverter;
import com.order.bachlinh.server.component.converter.spi.ProtobuffConverter;
import com.order.bachlinh.server.handler.annotation.QuicStreamChannel;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.incubator.codec.http3.DefaultHttp3DataFrame;
import io.netty.incubator.codec.http3.DefaultHttp3HeadersFrame;
import io.netty.incubator.codec.http3.Http3DataFrame;
import io.netty.incubator.codec.http3.Http3Headers;
import io.netty.incubator.codec.http3.Http3HeadersFrame;
import io.netty.incubator.codec.http3.Http3RequestStreamInboundHandler;
import org.springframework.core.annotation.Order;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@QuicStreamChannel(name = "convertAndForwardRequest")
@Order(value = 0)
public class ConvertAndForwardRequestHandler extends Http3RequestStreamInboundHandler {
    private static final String REAL_SERVER_URL = "localhost:8080";
    private final Map<Long, byte[]> headerMap = Collections.synchronizedMap(new HashMap<>());
    private final RestTemplate restTemplate;
    private Http3FrameRequestConverter frameRequestConverter;
    private ProtobuffConverter protobuffConverter;

    public ConvertAndForwardRequestHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3HeadersFrame frame, boolean isLast) throws Exception {
        long streamId = ((io.netty.incubator.codec.quic.QuicStreamChannel) ctx.channel()).streamId();
        MultiValueMap<String, String> headers = protobuffConverter.convertToHeader(frame);
        if (isLast) {
            forwardHeader(ctx, headers);
        }
        headerMap.put(streamId, serializeData(headers));
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3DataFrame frame, boolean isLast) throws Exception {

    }

    private byte[] serializeData(Object data) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(data);
        return byteOut.toByteArray();
    }

    private void forwardHeader(ChannelHandlerContext ctx, MultiValueMap<String, String> headers) throws IOException {
        String path = headers.get(Http3Headers.PseudoHeaderName.PATH.value().toString()).get(0);
        switch (Http3Headers.PseudoHeaderName.METHOD.value().toString()) {
            case "GET" -> {
                ByteBuffer resp = restTemplate.get(REAL_SERVER_URL + path, ByteBuffer.allocate(0), headers, Collections.emptyMap());
                sendResponse(ctx, resp);
            }
            case "POST" -> {
                ByteBuffer resp = restTemplate.post(REAL_SERVER_URL + path, ByteBuffer.allocate(0), headers, Collections.emptyMap());
                sendResponse(ctx, resp);
            }
            case "PUT" -> {
                ByteBuffer resp = restTemplate.put(REAL_SERVER_URL + path, ByteBuffer.allocate(0), headers, Collections.emptyMap());
                sendResponse(ctx, resp);
            }
            case "DELETE" -> {
                ByteBuffer resp = restTemplate.delete(REAL_SERVER_URL + path, ByteBuffer.allocate(0), headers, Collections.emptyMap());
                sendResponse(ctx, resp);
            }
            default -> {
                String notFound = "Not Found";
                Http3HeadersFrame headersFrame = new DefaultHttp3HeadersFrame();
                headersFrame.headers().status("404");
                headersFrame.headers().add("server", "netty");
                headersFrame.headers().addInt("content-length", notFound.length());
                ctx.write(headersFrame);
                ctx.writeAndFlush(new DefaultHttp3DataFrame(Unpooled.wrappedBuffer(notFound.getBytes())));
            }
        }

    }

    private void sendResponse(ChannelHandlerContext ctx, ByteBuffer source) {
        Http3DataFrame body = frameRequestConverter.convertDataFrame(source);
        Http3HeadersFrame headersFrame = frameRequestConverter.convertHeaderFrame(source);
        ctx.write(headersFrame);
        ctx.writeAndFlush(body);
    }
}
