package com.order.bachlinh.core.util.monitor;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Log4j2
class RepositoryMonitor {

    @Pointcut("@within(org.springframework.stereotype.Repository)")
    private void repositoryPointcut() {
    }

    @Pointcut("execution(public * *(..))")
    private void repositoryPublicMethod() {
    }

    @Around("repositoryPointcut() && repositoryPublicMethod()")
    public Object monitorRepository(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodExecuteName = proceedingJoinPoint.getSignature().toShortString();
        StopWatch watch = new StopWatch();
        log.info("Begin execute repository method name: {}", methodExecuteName);
        watch.start();
        Object result = proceedingJoinPoint.proceed();
        watch.stop();
        log.info("Finish process repository method in {} ms", watch.getTotalTimeMillis());
        return result;
    }
}
