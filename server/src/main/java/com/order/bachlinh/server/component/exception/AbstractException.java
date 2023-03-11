package com.order.bachlinh.server.component.exception;

import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {
    private final int status;

    protected AbstractException(String msg, Throwable cause, int status) {
        super(msg, cause);
        this.status = status;
    }

    protected AbstractException(String msg, int status) {
        this(msg, null, status);
    }
}
