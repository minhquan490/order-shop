package com.order.bachlinh.core.entities.spi;

import com.order.bachlinh.core.entities.model.BaseEntity;

import java.util.Collection;

/**
 * The context for hold the entity, {@link EntityValidator} for validate the entity,
 * caching region for define the cache storage name.
 *
 * @author Hoang Minh Quan
 * */
public interface EntityContext extends IdContext {

    /**
     * Return the entity associate with its context.
     *
     * @return The entity
     * */
    BaseEntity getEntity();

    /**
     * Return the validator of entity.
     *
     * @return collection of entity validators
     * */
    Collection<EntityValidator> getValidators();

    /**
     * Return the cache region of entity
     *
     * @return cache region
     * */
    String getCacheRegion();
}
