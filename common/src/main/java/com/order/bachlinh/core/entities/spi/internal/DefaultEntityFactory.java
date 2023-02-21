package com.order.bachlinh.core.entities.spi.internal;

import com.order.bachlinh.core.entities.spi.EntityContext;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.util.EntityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
final class DefaultEntityFactory implements EntityFactory {

    private final Map<Class<?>, EntityContext> entityContext;
    private final ApplicationContext applicationContext;

    @Override
    public <T> boolean containsEntity(Class<T> entityType) {
        return entityContext.containsKey(entityType);
    }

    @Override
    public <T> T getEntity(Class<T> entityType) {
        if (containsEntity(entityType)) {
            return entityType.cast(entityContext.get(entityType).getEntity());
        }
        return null;
    }

    @Override
    public EntityContext getEntityContext(Class<?> entityType) {
        return entityContext.get(entityType);
    }

    static class DefaultEntityFactoryBuilder implements EntityFactoryBuilder {
        private ApplicationContext applicationContext;

        @Override
        public EntityFactoryBuilder applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        @Override
        public EntityFactory build() {
            Map<Class<?>, EntityContext> entityContext = new HashMap<>();
            Collection<Class<?>> entities;
            try {
                entities = EntityUtils.scanPackageEntity(applicationContext);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
                entities = Collections.emptySet();
            }
            entities.forEach(entity -> entityContext.put(entity, new DefaultEntityContext(entity, applicationContext)));
            return new DefaultEntityFactory(entityContext, applicationContext);
        }
    }
}
