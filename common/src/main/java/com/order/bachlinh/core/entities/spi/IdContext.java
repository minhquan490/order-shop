package com.order.bachlinh.core.entities.spi;

/**
 * Context id associate with entity for create id entity
 * without fetching a database.
 *
 * @author Hoang Minh Quan.
 * */
public interface IdContext {

    /**
     * Return the next id context.
     *
     * @return the next id.
     * */
    Object getNextId();

    /**
     * Revert the id into context.
     * */
    void rollback();
}
