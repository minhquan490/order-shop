package com.order.bachlinh.server.component.converter.spi;

import io.netty.incubator.codec.http3.Http3DataFrame;
import io.netty.incubator.codec.http3.Http3HeadersFrame;
import org.springframework.util.MultiValueMap;

import java.nio.ByteBuffer;

public interface ProtobuffConverter {
    ByteBuffer convertToProtoBody(Http3DataFrame dataFrame);
    MultiValueMap<String, String> convertToHeader(Http3HeadersFrame http3HeadersFrame);
}
