package ru.kpfu.itis.lobanov.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

//    @Pointcut("execution(*. ru.kpfu.itis.lobanov..*.*.*(..))")
//    public void logExecutionTime() {
//
//    }

    @Pointcut("@annotation(ru.kpfu.itis.lobanov.aspect.annotations.Loggable)")
    public void logExecutionTime() {

    }

    @Around("logExecutionTime()")
    public Object log(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();

       final StopWatch stopWatch = new StopWatch();
       stopWatch.start();

        Object result;
       try {
           result = joinPoint.proceed();
       } catch (Throwable e) {
           throw new RuntimeException(e);
       }
       stopWatch.stop();
       log.info("Execution time of {}.{} = {} ms", className, methodName, stopWatch.getTotalTimeMillis());
       return result;
    }
}
