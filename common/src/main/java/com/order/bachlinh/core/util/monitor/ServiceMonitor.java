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
class ServiceMonitor {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    private void servicePointcut() {
    }

    @Pointcut("execution(public * *(..))")
    private void servicePublicMethod() {
    }

    @Around("servicePointcut() && servicePublicMethod()")
    public Object monitorService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodExecuteName = proceedingJoinPoint.getSignature().toShortString();
        StopWatch watch = new StopWatch();
        log.info("Begin execute service method {}", methodExecuteName);
        watch.start();
        Object result = proceedingJoinPoint.proceed();
        watch.stop();
        log.info("Finish process service method in {} ms", watch.getTotalTimeMillis());
        return result;
    }
}
