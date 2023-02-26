package com.order.bachlinh.core.entities.spi.interceptor;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.spi.EntityTrigger;
import com.order.bachlinh.core.entities.spi.TriggerMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Aspect
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 3)
@Log4j2
public class TriggerInterceptor {
    private final EntityFactory entityFactory;

    @Pointcut("@within(org.springframework.stereotype.Repository)")
    private void triggerOn() {}

    @Pointcut("execution(public save*(..))")
    private void triggerOnSavePointcut() {}

    @Pointcut("execution(public update*(..))")
    private void triggerOnUpdatePointcut() {}

    @Pointcut("execution(public edit*(..))")
    private void triggerOnEditPointcut() {}

    @Pointcut("execution(public delete*(..))")
    private void triggerOnDeletePointcut() {}

    @Around("triggerOn() && (triggerOnSavePointcut() || triggerOnUpdatePointcut() || triggerOnEditPointcut() || triggerOnDeletePointcut())")
    public Object trigger(ProceedingJoinPoint joinPoint) throws Throwable {
        BaseEntity baseEntity = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseEntity casted) {
                baseEntity = casted;
            }
        }
        if (baseEntity == null) {
            return joinPoint.proceed();
        }
        Collection<EntityTrigger> triggers = entityFactory.getEntityContext(baseEntity.getClass()).getTrigger();
        log.info("Begin BEFORE trigger -----");
        executeBeforeAction(triggers, baseEntity);
        log.info("End BEFORE trigger -----");
        Object result = joinPoint.proceed();
        log.info("Begin AFTER trigger -----");
        executeAfterAction(triggers, baseEntity);
        log.info("End AFTER trigger -----");
        return result;
    }

    private void executeBeforeAction(Collection<EntityTrigger> triggers, BaseEntity entity) {
        Collection<EntityTrigger> beforeAction = triggers.stream().filter(trigger -> trigger.getMode().equals(TriggerMode.BEFORE)).toList();
        beforeAction.forEach(trigger -> trigger.execute(entity));
    }

    private void executeAfterAction(Collection<EntityTrigger> triggers, BaseEntity entity) {
        Collection<EntityTrigger> beforeAction = triggers.stream().filter(trigger -> trigger.getMode().equals(TriggerMode.AFTER)).toList();
        beforeAction.forEach(trigger -> trigger.execute(entity));
    }
}
