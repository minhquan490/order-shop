package com.order.bachlinh.server.component.handler;

import com.order.bachlinh.core.component.client.java.internal.HttpClientFactoryBuilderProvider;
import com.order.bachlinh.core.component.client.java.spi.HttpClientFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.NonNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

public final class PathMatchHandler extends ChannelInboundHandlerAdapter {
    private static final String CLIENT_URL;
    private static final HttpClient singletonClient;

    static {
        HttpClientFactory.Builder builder = HttpClientFactoryBuilderProvider.getBuilder();
        builder.connectTimeout(Duration.ofSeconds(10));
        singletonClient = builder.buildFactory().createJavaCoreClient();
        CLIENT_URL = System.getProperty("server.frontend.url");
    }

    @Override
    public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) throws Exception {
        if (msg instanceof HttpRequest req) {
            switch (req.method()) {
                case "GET" -> {
                    Optional<String> contentType = req.headers().firstValue(HttpHeaderNames.CONTENT_TYPE.toString());
                    if (contentType.isPresent()) {
                        super.channelRead(ctx, msg);
                        return;
                    }
                    HttpRequest newRequest = replaceUrl(req, CLIENT_URL.concat(req.uri().toString()));
                    HttpResponse<?> response = singletonClient.send(newRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                    ctx.write(response);
                }
                case "POST", "PUT", "DELETE" -> super.channelRead(ctx, msg);
                default -> ctx.fireExceptionCaught(new UnsupportedOperationException("Only support method GET, POST, PUT, DELETE"));
            }
            return;
        }
        super.channelRead(ctx, msg);
    }

    private HttpRequest replaceUrl(HttpRequest old, String url) {
        Optional<HttpRequest.BodyPublisher> oldBody = old.bodyPublisher();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));
        old.headers().map().forEach((key, value) -> value.forEach(headerValue -> builder.header(key, headerValue)));
        return builder.method(old.method(), oldBody.orElseGet(HttpRequest.BodyPublishers::noBody))
                .build();
    }
}
