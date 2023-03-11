package com.order.bachlinh.server.component.exception;

public class MethodNotSupportedException extends AbstractException {
    public MethodNotSupportedException(String msg, Throwable cause, int status) {
        super(msg, cause, status);
    }

    public MethodNotSupportedException(String msg, int status) {
        super(msg, status);
    }
}
