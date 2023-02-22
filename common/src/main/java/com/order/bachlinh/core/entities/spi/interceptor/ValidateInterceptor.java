package com.order.bachlinh.core.entities.spi.interceptor;

import com.order.bachlinh.core.annotation.Validated;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.exception.ConstraintViolationException;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.EntityValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The aspect for intercept the method to validate entity before save.
 *
 * @author Hoang Minh Quan
 * */
@Component
@Aspect
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class ValidateInterceptor {
    private final EntityFactory entityFactory;

    @Pointcut("@annotation(com.order.bachlinh.core.annotation.Validated)")
    private void validatePointcut() {
    }

    @Before(value = "validatePointcut()", argNames = "joinPoint")
    public void validate(JoinPoint joinPoint) {
        BaseEntity entity = findEntity(joinPoint);
        Collection<EntityValidator> validators = entityFactory.getEntityContext(entity.getClass()).getValidators();
        if (validators.isEmpty()) {
            return;
        }
        doValidate(validators, entity);
    }

    private void doValidate(Collection<EntityValidator> validators, BaseEntity entity) {
        Set<String> errors = new HashSet<>();
        validators.forEach(entityValidator -> entityValidatorCallback(entityValidator, errors, entity));
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }

    private void entityValidatorCallback(EntityValidator entityValidator, Set<String> errors, BaseEntity entity) {
        ValidateResult result = entityValidator.validate(entity);
        if (result.hasError()) {
            errors.addAll(result.getMessages());
        }
    }

    private BaseEntity findEntity(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method adviceMethod = methodSignature.getMethod();
        Validated validated = adviceMethod.getAnnotation(Validated.class);
        return (BaseEntity) joinPoint.getArgs()[validated.targetIndex()];
    }
}
