package com.microservices.profile_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

// Define this class as an Aspect for cross-cutting concerns
@Aspect
@Component
public class LoggingAspect {

    // Logger to log messages
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Define an Around advice to log method execution details for ProfileServiceImpl class
    @Around("execution(* com.microservices.profile_service.service.impl.ProfileServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Log the method entry and its arguments
        logger.info("Entering method: " + joinPoint.getSignature().getName() + " with arguments: " + Arrays.toString(joinPoint.getArgs()));
        
        // Proceed with the method execution
        Object result = joinPoint.proceed();
        
        // Log the method exit and its result
        logger.info("Exiting method: " + joinPoint.getSignature().getName() + " with result: " + result);
        
        // Return the result to the caller
        return result;
    }
}
