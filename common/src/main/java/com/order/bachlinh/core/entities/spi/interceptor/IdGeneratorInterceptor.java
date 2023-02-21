package com.order.bachlinh.core.entities.spi.interceptor;

import com.order.bachlinh.core.entities.spi.EntityContext;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.model.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;

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
    public Object accept(ProceedingJoinPoint joinPoint) {
        BaseEntity entity = findEntityParam(joinPoint);
        EntityContext context = factory.getEntityContext(entity.getClass());
        if (entity.getId() == null) {
            entity.setId(context.getNextId());
        }
        try {
            return joinPoint.proceed(Collections.singleton(entity).toArray());
        } catch (Throwable e) {
            context.rollback();
            throw new IllegalStateException("Can not process join point for entity [" + entity.getClass().getSimpleName() + "]", e);
        }
    }

    private BaseEntity findEntityParam(ProceedingJoinPoint joinPoint) {
        BaseEntity entity = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg.getClass().isAssignableFrom(BaseEntity.class)) {
                entity = (BaseEntity) arg;
            }
        }
        if (entity == null) {
            throw new IllegalArgumentException("Use @IdentifierGenerator on save method only");
        }
        return entity;
    }
}
