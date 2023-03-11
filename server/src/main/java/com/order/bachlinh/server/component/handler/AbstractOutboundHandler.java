package com.order.bachlinh.server.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.springframework.util.MultiValueMap;

import javax.net.ssl.SSLSession;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public abstract class AbstractOutboundHandler<RESPONSE, TRANSFER> extends ChannelOutboundHandlerAdapter {
    private final Class<RESPONSE> responseClass;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    protected AbstractOutboundHandler() {
        this.responseClass = (Class<RESPONSE>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.objectMapper = HttpConventionHandler.getSingleton();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg.getClass().isAssignableFrom(responseClass)) {
            TRANSFER data = processResponse((RESPONSE) msg);
            HttpResponse<String> response = new DummyHttpResponse<>(objectMapper.writeValueAsString(data), lookupHeader());
            super.write(ctx, response, promise);
        }
        super.write(ctx, msg, promise);
    }

    protected abstract TRANSFER processResponse(RESPONSE response);

    protected abstract MultiValueMap<String, String> lookupHeader();

    private record DummyHttpResponse<T>(T body, MultiValueMap<String, String> h) implements HttpResponse<T> {

        @Override
        public int statusCode() {
            return 200;
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<T>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return HttpHeaders.of(h, (a, b) -> true);
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return null;
        }

        @Override
        public HttpClient.Version version() {
            return null;
        }
    }
}
