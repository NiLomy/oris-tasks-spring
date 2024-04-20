package ru.kpfu.itis.lobanov.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
 * Aspect for logging usage count of business methods
 * Create instance of aspect for each method
 */

@Aspect("perthis(@within(org.springframework.stereotype.Service))")
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class UsageCountAspect {
    private int usageCount = 0;

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void logUsageCount() {
    }

    @Around("logUsageCount()")
    public Object log(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String methodName = methodSignature.getName();
        usageCount++;

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        log.info("Method {} was used {} times.", methodName, usageCount);
        return result;
    }
}
