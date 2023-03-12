package com.order.bachlinh.server.component.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import javax.net.ssl.SSLSession;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Optional;

public abstract class AbstractOutboundHandler<RESPONSE> extends ChannelOutboundHandlerAdapter {
    private final Class<RESPONSE> responseClass;

    @SuppressWarnings("unchecked")
    protected AbstractOutboundHandler() {
        this.responseClass = (Class<RESPONSE>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg.getClass().isAssignableFrom(responseClass)) {
            Object data = processResponse((RESPONSE) msg);
            HttpResponse<?> response;
            if (data instanceof ResponseEntity<?> res) {
                response = new DummyHttpResponse<>(res.getBody(), res.getHeaders(), res.getStatusCode().value());
            } else {
                response = new DummyHttpResponse<>(data, lookupHeader(), lookupStatusCode());
            }
            super.write(ctx, response, promise);
        }
        super.write(ctx, msg, promise);
    }

    protected abstract Object processResponse(RESPONSE response);

    protected MultiValueMap<String, String> lookupHeader() {
        return new MultiValueMapAdapter<>(new HashMap<>());
    }

    protected int lookupStatusCode() {
        return 200;
    }

    private record DummyHttpResponse<T>(T body, MultiValueMap<String, String> h, int statusCode) implements HttpResponse<T> {

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
