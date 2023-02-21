package com.order.bachlinh.core.entities.spi.internal;

import com.order.bachlinh.core.annotation.Label;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.model.AbstractEntity;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.EntityContext;
import com.order.bachlinh.core.entities.spi.EntityValidator;
import jakarta.persistence.PersistenceException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
class DefaultEntityContext implements EntityContext {

    private final BaseEntity baseEntity;
    private final String prefix;
    private final String cacheRegion;
    private final List<EntityValidator> validators;
    private int previousId;

    DefaultEntityContext(Class<?> entity, ApplicationContext context) {
        try {
            log.info("Init entity context for entity {}", entity.getSimpleName());
            this.baseEntity = (BaseEntity) UnsafeUtils.getUnsafe().allocateInstance(entity);
            this.validators = getValidators(entity, context);
            this.previousId = 1;
            Label label = entity.getAnnotation(Label.class);
            if (label != null) {
                this.prefix = entity.getAnnotation(Label.class).value();
            } else {
                this.prefix = null;
            }
            Cache cache = entity.getAnnotation(Cache.class);
            if (cache != null) {
                this.cacheRegion = cache.region();
            } else {
                this.cacheRegion = null;
            }
            log.info("Init complete");
        } catch (Exception e) {
            throw new PersistenceException("Can not instance entity with type [" + entity.getSimpleName() + "]", e);
        }
    }

    @Override
    public BaseEntity getEntity() {
        try {
            return (BaseEntity) ((AbstractEntity) baseEntity).clone();
        } catch (CloneNotSupportedException e) {
            // This will never cause
            return null;
        }
    }

    @Override
    public Collection<EntityValidator> getValidators() {
        return validators;
    }

    @Override
    public String getCacheRegion() {
        return cacheRegion;
    }

    @Override
    public Object getNextId() {
        this.previousId += 1;
        return prefix + String.format("%06d", previousId);
    }

    @Override
    public void rollback() {
        this.previousId -= 1;
    }

    private List<EntityValidator> getValidators(Class<?> entity, ApplicationContext context) {
        Validator v = entity.getAnnotation(Validator.class);
        if (v == null) {
            return null;
        }
        List<EntityValidator> vs = new ArrayList<>();
        try {
            for (Class<?> validator : v.validators()) {
                vs.add((EntityValidator) validator.getConstructor(ApplicationContext.class).newInstance(context));
            }
        } catch (Exception e) {
            // Ignore
        }
        return vs;
    }
}
