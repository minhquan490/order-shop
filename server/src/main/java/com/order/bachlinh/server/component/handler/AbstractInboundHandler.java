package com.order.bachlinh.server.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;

public abstract class AbstractInboundHandler<REQUEST, RESPONSE> extends ChannelInboundHandlerAdapter {
    private final ObjectMapper objectMapper;
    private final Class<REQUEST> requestBodyType;

    @SuppressWarnings("unchecked")
    protected AbstractInboundHandler() {
        this.objectMapper = HttpConventionHandler.getSingleton();
        this.requestBodyType = (Class<REQUEST>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    @Override
    public final void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) throws Exception {
        if (msg instanceof HttpRequest request) {
            Optional<String> bodyExtracted = request.bodyPublisher().map(p -> {
                var bodySubscriber = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
                var flowSubscriber = new StringSubscriber(bodySubscriber);
                p.subscribe(flowSubscriber);
                return bodySubscriber.getBody().toCompletableFuture().join();
            });
            if (bodyExtracted.isPresent()) {
                var body = bodyExtracted.get();
                var psudoRequest = objectMapper.readValue(body, requestBodyType);
                var psudoResponse = processRequest(psudoRequest);
                ctx.write(psudoResponse);
                return;
            }
        }
        super.channelRead(ctx, msg);
    }

    protected abstract RESPONSE processRequest(REQUEST request);

    private record StringSubscriber(HttpResponse.BodySubscriber<String> wrapped) implements Flow.Subscriber<ByteBuffer> {
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            wrapped.onSubscribe(subscription);
        }

        @Override
        public void onNext(ByteBuffer item) {
            wrapped.onNext(List.of(item));
        }

        @Override
        public void onError(Throwable throwable) {
            wrapped.onError(throwable);
        }

        @Override
        public void onComplete() {
            wrapped.onComplete();
        }
    }
}
