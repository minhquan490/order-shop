package com.order.bachlinh.server.handler.internal;

import com.google.common.reflect.ClassPath;
import com.order.bachlinh.server.ServerApplication;
import com.order.bachlinh.server.boot.spi.ChannelDecorator;
import com.order.bachlinh.server.handler.annotation.HandlerInitializer;
import com.order.bachlinh.server.handler.annotation.QuicChannel;
import com.order.bachlinh.server.handler.annotation.QuicStreamChannel;
import com.order.bachlinh.server.handler.spi.ChannelDecoratorProvider;
import com.order.bachlinh.server.handler.spi.ChannelHandlerLookup;
import com.order.bachlinh.server.handler.spi.ConstructorParameterProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
class InternalHandler implements ChannelHandlerLookup, ChannelDecoratorProvider {
    private final String handlerLocation;
    private final ConstructorParameterProvider parameterProvider;

    InternalHandler(String handlerPackage) {
        log.info("Init InternalHandler for handler package [{}]", handlerPackage);
        this.handlerLocation = handlerPackage;
        this.parameterProvider = new InternalConstructorParameterProvider();
    }

    @Override
    public Collection<ChannelHandler> lookupHandlers(String location) {
        log.info("Begin find handler in package [{}]", location);
        try {
            List<ChannelHandler> handlers = ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream()
                    .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(location))
                    .map(ClassPath.ClassInfo::load)
                    .filter(clazz -> clazz.isAssignableFrom(ChannelHandler.class))
                    .filter(clazz -> clazz.isAnnotationPresent(QuicChannel.class) || clazz.isAnnotationPresent(QuicStreamChannel.class))
                    .sorted(Comparator.comparing(clazz -> {
                        Order order = clazz.getAnnotation(Order.class);
                        if (order == null) {
                            return Ordered.LOWEST_PRECEDENCE;
                        }
                        return order.value();
                    }))
                    .map(this::newInstance)
                    .map(ChannelHandler.class::cast)
                    .collect(Collectors.toList());
            Collections.reverse(handlers);
            return handlers;
        } catch (IOException | IllegalStateException e) {
            log.debug("Exception occur with type [{}], message: [{}]", e.getClass().getName(), e.getMessage());
            return Collections.emptyList();
        } finally {
            log.info("Finding handler complete");
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
                    log.debug("Constructor of [{}] is private, make it accessible !", target.getName());
                    con.setAccessible(true);
                }
                return con;
            }
        }
        log.debug("Constructor marked by annotation [{}] not found, fallback to default constructor", marker.getName());
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

    private static class InternalConstructorParameterProvider implements ConstructorParameterProvider {
        private final Map<Class<?>[], Object[]> paramTypeCache = new HashMap<>();
        private final ApplicationContext springContext;

        InternalConstructorParameterProvider() {
            this.springContext = new AnnotationConfigApplicationContext(ServerApplication.class.getPackageName(), "com.order.bachlinh.core.configuration");
        }

        @Override
        public Object[] getParameters(Class<?>[] paramType) {
            Object[] result = paramTypeCache.get(paramType);
            if (result == null) {
                log.debug("Object value of types [{}] not found, init its", Stream.of(paramType).toList());
                initObjects(paramType);
                return getParameters(paramType);
            }
            return result;
        }

        private void initObjects(Class<?>[] types) {
            Collection<Object> result = new ArrayList<>();
            Collection<Class<?>> typeCollections = Stream.of(types).toList();
            typeCollections.forEach(type -> {
                log.info("Begin instance object of type [{}]", type.getName());
                Constructor<?> constructor;
                try {
                    constructor = type.getConstructor();
                    result.add(constructor.newInstance());
                    log.info("Instance complete");
                } catch (NoSuchMethodException e) {
                    log.debug("Default constructor of type [{}] not found, fallback to spring bean", type.getName());
                    result.add(springContext.getBean(type));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e1) {
                    throw new IllegalStateException("Can not instance class [" + type.getName() + "]", e1);
                }
            });
            paramTypeCache.put(types, result.toArray());
            log.info("Instance complete");
        }
    }
}
