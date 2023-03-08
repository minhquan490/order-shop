package com.order.bachlinh.server.handler.internal;

import com.google.common.reflect.ClassPath;
import com.order.bachlinh.server.boot.spi.ChannelDecorator;
import com.order.bachlinh.server.handler.annotation.HandlerInitializer;
import com.order.bachlinh.server.handler.annotation.QuicChannel;
import com.order.bachlinh.server.handler.annotation.QuicStreamChannel;
import com.order.bachlinh.server.handler.spi.ChannelDecoratorProvider;
import com.order.bachlinh.server.handler.spi.ChannelHandlerLookup;
import com.order.bachlinh.server.handler.spi.ConstructorParameterProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class InternalHandler implements ChannelHandlerLookup, ChannelDecoratorProvider {
    private final String handlerLocation;
    private final ConstructorParameterProvider parameterProvider;

    @Override
    public Collection<ChannelHandler> lookupHandlers(String location) {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream()
                    .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(location))
                    .map(ClassPath.ClassInfo::load)
                    .filter(clazz -> clazz.isAnnotationPresent(QuicChannel.class) || clazz.isAnnotationPresent(QuicStreamChannel.class))
                    .map(this::newInstance)
                    .map(ChannelHandler.class::cast)
                    .collect(Collectors.toSet());
        } catch (IOException | IllegalStateException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<ChannelDecorator<? extends Channel>> getChannelDecorator() {
        Collection<ChannelDecorator<? extends Channel>> decorators = new ArrayList<>();
        Collection<ChannelHandler> channelHandlers = lookupHandlers(handlerLocation);
        channelHandlers.forEach(handler -> {
            Class<?> handlerClass = handler.getClass();
            if (handlerClass.isAnnotationPresent(QuicChannel.class)) {
                QuicChannel quicChannel = handlerClass.getAnnotation(QuicChannel.class);
                decorators.add((io.netty.incubator.codec.quic.QuicChannel quic) -> quic.pipeline().addLast(quicChannel.name(), handler));
            }
            if (handlerClass.isAnnotationPresent(QuicStreamChannel.class)) {
                QuicStreamChannel quicStreamChannel = handlerClass.getAnnotation(QuicStreamChannel.class);
                decorators.add((io.netty.incubator.codec.quic.QuicStreamChannel quic) -> quic.pipeline().addLast(quicStreamChannel.name(), handler));
            }
        });
        return decorators;
    }

    private Constructor<?> getConstructor(Class<?> target, Class<? extends Annotation> marker) throws NoSuchMethodException {
        Constructor<?>[] constructors = target.getDeclaredConstructors();
        for (Constructor<?> con : constructors) {
            if (con.isAnnotationPresent(marker)) {
                if (Modifier.isPrivate(con.getModifiers())) {
                    con.setAccessible(true);
                }
                return con;
            }
        }
        return target.getConstructor();
    }

    @SuppressWarnings("unchecked")
    private <T> T newInstance(Class<T> target) {
        try {
            Constructor<?> constructor = getConstructor(target, HandlerInitializer.class);
            return (T) constructor.newInstance(parameterProvider.getParameters(constructor.getParameterTypes()));
        } catch (Exception e) {
            throw new IllegalStateException("Can not instance class with type [" + target.getName() + "]", e);
        }
    }
}
