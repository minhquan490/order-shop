package com.order.bachlinh.core.entities.spi;

import java.util.Collection;

/**
 * Validate result holder for holding result after validation.
 *
 * @author Hoang Minh Quan
 * */
public interface ValidateResult {

    /**
     * Check validation has error.
     *
     * @return true, if it has error.
     * */
    boolean hasError();

    /**
     * Return error messages when a result has error
     *
     * @return messages error
     * */
    Collection<String> getMessages();
}
