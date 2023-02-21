package com.order.bachlinh.core.exception;

/**
 * Base exception for all exceptions used in this project.
 *
 * @author Hoang Minh Quan
 * */
public abstract class ApplicationException extends RuntimeException {
    protected ApplicationException(String msg) {
        super(msg);
    }

    protected ApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
