package com.order.bachlinh.core.entities.spi.interceptor;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.EntityContext;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import jakarta.persistence.GeneratedValue;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The aspect for intercept method mark by {@link com.order.bachlinh.core.annotation.IdentifierGenerator}
 * for generate id before save into a database.
 *
 * @author Hoang Minh Quan
 * */
@Component
@Aspect
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class IdGeneratorInterceptor {
    private final EntityFactory factory;

    @Pointcut("@annotation(com.order.bachlinh.core.annotation.IdentifierGenerator)")
    private void intercept() {
    }

    /**
     * Inject id for entity before save into a database.
     * */
    @Around("intercept()")
    public Object accept(ProceedingJoinPoint joinPoint) throws Throwable {
        Collection<BaseEntity> entities = findEntityParams(joinPoint);
        EntityContext context = null;
        for (BaseEntity entity : entities) {
            context = factory.getEntityContext(entity.getClass());
            if (hasGeneratedValue(entity)) {
                continue;
            }
            if (entity.getId() == null) {
                entity.setId(context.getNextId());
            }
        }
        try {
            return joinPoint.proceed(entities.toArray());
        } catch (Throwable e) {
            if (context != null) {
                context.rollback();
            }
            throw new IllegalStateException("Has problem when generate id", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<BaseEntity> findEntityParams(ProceedingJoinPoint joinPoint) {
        Set<BaseEntity> entities = new HashSet<>();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg.getClass().isAssignableFrom(BaseEntity.class)) {
                entities.add((BaseEntity) arg);
            }
            if (arg.getClass().isAssignableFrom(Collection.class)) {
                // Get a generic type from a collection
                // Example Collection<T> -> get T and check type of it
                Class<?> entityType = (Class<?>) ((ParameterizedType) arg.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                if (entityType.isAssignableFrom(BaseEntity.class)) {
                    entities.addAll(((Collection<BaseEntity>) arg));
                }
            }
        }
        return entities;
    }

    private boolean hasGeneratedValue(BaseEntity entity) {
        return entity.getClass().getAnnotation(GeneratedValue.class) != null;
    }
}
