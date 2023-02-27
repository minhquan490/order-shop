package com.order.bachlinh.core.entities.spi.internal;

import com.order.bachlinh.core.annotation.Label;
import com.order.bachlinh.core.annotation.Trigger;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.model.AbstractEntity;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.EntityContext;
import com.order.bachlinh.core.entities.spi.EntityTrigger;
import com.order.bachlinh.core.entities.spi.EntityValidator;
import com.order.bachlinh.core.entities.spi.TriggerMode;
import jakarta.persistence.PersistenceException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.objenesis.SpringObjenesis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Log4j2
class DefaultEntityContext implements EntityContext {

    private final BaseEntity baseEntity;
    private final String prefix;
    private final String cacheRegion;
    private final List<EntityValidator> validators;
    private final List<EntityTrigger> triggers;
    private int previousId;
    private int createIdTime = 0;

    DefaultEntityContext(Class<?> entity, ApplicationContext context) {
        SpringObjenesis springObjenesis = new SpringObjenesis();
        try {
            log.info("Init entity context for entity {}", entity.getSimpleName());
            this.baseEntity = (BaseEntity) springObjenesis.newInstance(entity);
            this.validators = getValidators(entity, context);
            this.triggers = getTriggers(entity, context);
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
    public Collection<EntityTrigger> getTrigger() {
        return triggers;
    }

    @Override
    public String getCacheRegion() {
        return cacheRegion;
    }

    @Override
    public Object getNextId() {
        this.previousId += 1;
        this.createIdTime += 1;
        return prefix + String.format("%06d", previousId);
    }

    @Override
    public void rollback() {
        this.previousId -= createIdTime;
        this.createIdTime = 0;
    }

    private List<EntityValidator> getValidators(Class<?> entity, ApplicationContext context) {
        Validator v = entity.getAnnotation(Validator.class);
        if (v == null) {
            return Collections.emptyList();
        }
        List<EntityValidator> vs = new ArrayList<>();
        try {
            for (Class<?> validator : v.validators()) {
                vs.add((EntityValidator) validator.getDeclaredConstructor(ApplicationContext.class).newInstance(context));
            }
        } catch (Exception e) {
            log.error("Can not create validator", e);
        }
        return vs;
    }

    private List<EntityTrigger> getTriggers(Class<?> entity, ApplicationContext context) {
        Trigger trigger = entity.getAnnotation(Trigger.class);
        if (trigger == null) {
            return Collections.emptyList();
        }
        List<EntityTrigger> entityTriggers = new ArrayList<>();
        try {
            for (Class<? extends EntityTrigger> t : trigger.triggers()) {
                EntityTrigger triggerObject = t.getDeclaredConstructor(ApplicationContext.class, TriggerMode.class).newInstance(context, trigger.mode());
                entityTriggers.add(triggerObject);
            }
        } catch (Exception e) {
            log.error("Can not create trigger", e);
        }
        return entityTriggers;
    }
}
