package com.order.bachlinh.server.component.handler;

import com.order.bachlinh.server.component.exception.AbstractException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExceptionTranslateHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        HttpResponse<?> response;
        Map<String, Object> body = new HashMap<>();
        GlobalException globalException;
        if (cause instanceof AbstractException translated) {
            globalException = new GlobalException(translated.getMessage(), translated.getCause(), translated.getStatus());
        } else {
            globalException = new GlobalException(cause.getMessage(), cause, HttpResponseStatus.BAD_GATEWAY.code());
        }
        body.put("status", globalException.getStatus());
        body.put("message", globalException.getMessage());
        response = new DummyHttpResponse<>(body, new MultiValueMapAdapter<>(new HashMap<>()), globalException.getStatus());
        ctx.write(response);
    }

    @Log4j2
    private static class GlobalException extends AbstractException {
        GlobalException(String msg, Throwable cause, int status) {
            super(msg, cause, status);
            log.error(cause.getMessage(), cause);
        }
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
