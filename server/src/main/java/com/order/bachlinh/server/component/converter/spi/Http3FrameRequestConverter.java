package com.order.bachlinh.server.component.converter.spi;

import io.netty.incubator.codec.http3.Http3DataFrame;
import io.netty.incubator.codec.http3.Http3HeadersFrame;

import java.nio.ByteBuffer;

public interface Http3FrameRequestConverter {
    Http3DataFrame convertDataFrame(ByteBuffer data);

    Http3HeadersFrame convertHeaderFrame(ByteBuffer data);
}
