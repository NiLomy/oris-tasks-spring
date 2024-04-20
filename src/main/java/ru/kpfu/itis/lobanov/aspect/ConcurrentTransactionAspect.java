package ru.kpfu.itis.lobanov.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Component;

/*
 * Aspect for idempotent transactions
 * In case of db locks it tries to make transaction again
 */

@Aspect
@Order(2)
@Component
public class ConcurrentTransactionAspect {
    public static final int MAX_ATTEMPTS_COUNT = 3;

    @Pointcut("@annotation(ru.kpfu.itis.lobanov.aspect.annotations.Idempotent)")
    public void retryTransaction() {
    }

    @Around("retryTransaction()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) {
        int attempts = 0;
        PessimisticLockingFailureException lockingFailureException;

        do {
            attempts++;
            try {
                return joinPoint.proceed();
            } catch (PessimisticLockingFailureException e) {
                lockingFailureException = e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (attempts < MAX_ATTEMPTS_COUNT);

        throw lockingFailureException;
    }
}
