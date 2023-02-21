package com.order.bachlinh.core.exception;

/**
 * Throw when use a database not supported in this project.
 *
 * @author Hoang Minh Quan.
 * */
public class SqlDefinitionException extends ApplicationException {
    public SqlDefinitionException(String msg) {
        super(msg);
    }

    public SqlDefinitionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
