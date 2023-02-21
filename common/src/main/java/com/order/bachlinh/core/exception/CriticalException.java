package com.order.bachlinh.core.exception;

public class CriticalException extends ApplicationException {
    public CriticalException(String msg) {
        super(msg);
    }

    public CriticalException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
