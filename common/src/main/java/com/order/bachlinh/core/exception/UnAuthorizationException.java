package com.order.bachlinh.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Sub-exception of {@link AuthenticationException}, throw when authentication failure.
 *
 * @author Hoang Minh Quan
 * */
public class UnAuthorizationException extends AuthenticationException {
    public UnAuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnAuthorizationException(String msg) {
        super(msg);
    }
}
