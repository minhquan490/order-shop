package com.order.bachlinh.core.entities.spi;

import com.order.bachlinh.core.entities.model.BaseEntity;

/**
 * Validator for validate the entity before insert to a database.
 *
 * @author Hoang Minh Quan
 * */
public interface EntityValidator {

    /**
     * Validate the entity before save into a database.
     *
     * @param entity for validation.
     * @return result after validate entity.
     * */
    ValidateResult validate(BaseEntity entity);
}
