package com.loy124.myapp.core.util.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger("API_LOG");

    @Before("execution(* com.loy124.myapp..service..*(..))")
    public void logBeforeService(JoinPoint joinPoint) {
        String transactionId = ControllerLoggingAspect.getTransactionId();  // 트랜잭션 ID 가져오기
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("\n=== Service Start ===\n트랜잭션 ID: {}\nMethod: {}\n", transactionId, methodName);
    }

    @AfterReturning(pointcut = "execution(* com.loy124.myapp..service..*(..))")
    public void logAfterReturningService(JoinPoint joinPoint) {
        String transactionId = ControllerLoggingAspect.getTransactionId();  // 트랜잭션 ID 가져오기
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("\n=== Service End ===\n트랜잭션 ID: {}\nMethod: {}\n", transactionId, methodName);
    }
}
