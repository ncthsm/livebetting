package com.bilyoner.livebetting.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.bilyoner.livebetting.service.*.*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Method called: {} with args: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());

        Object result = null;
        try {
            result = joinPoint.proceed();

            log.info("Method executed successfully: {}, returned: {}", joinPoint.getSignature().getName(), result);
        } catch (Exception e) {
            log.error("Exception in method: {}, with message: {}", joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }

        log.info("Method execution finished: {}", joinPoint.getSignature().getName());

        return result;
    }

}
