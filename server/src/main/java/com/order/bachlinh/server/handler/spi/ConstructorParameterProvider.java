package com.order.bachlinh.server.handler.spi;

public interface ConstructorParameterProvider {
    Object[] getParameters(Class<?>[] paramType);
}
