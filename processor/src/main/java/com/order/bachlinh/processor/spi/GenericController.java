package com.order.bachlinh.processor.spi;

public interface GenericController<T, U, P> {

    T invoke(U target, P param) throws Throwable;
}
