package com.microservices.account_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Log method entry and exit points around the service layer methods
    @Around("execution(* com.microservices.account_service.service..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Entering method: {}", joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        logger.info("Exiting method: {}", joinPoint.getSignature().getName());
        return result;
    }
}
