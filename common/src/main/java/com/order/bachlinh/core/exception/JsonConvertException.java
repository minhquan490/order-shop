package com.order.bachlinh.core.exception;

/**
 * Throw when convert json to object failure.
 *
 * @author Hoang Minh Quan.
 * */
public class JsonConvertException extends ApplicationException {

    public JsonConvertException(String msg) {
        super(msg);
    }

    public JsonConvertException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
