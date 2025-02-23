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
public class RepositoryLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger("API_LOG");

    // 리포지토리 메서드 시작 시점 로깅
    @Before("execution(* com.loy124.myapp..repository..*(..))")
    public void logBeforeRepository(JoinPoint joinPoint) {
        String transactionId = ControllerLoggingAspect.getTransactionId();  // 트랜잭션 ID 가져오기
        String methodName = joinPoint.getSignature().toShortString();
        if (transactionId == null) {
            return;
        }
        logger.info("\n=== Repository Start ===\n트랜잭션 ID: {}\nMethod: {}\n",transactionId, methodName);
    }

    // 리포지토리 메서드 종료 시점 로깅
    @AfterReturning(pointcut = "execution(* com.loy124.myapp..repository..*(..))")
    public void logAfterReturningRepository(JoinPoint joinPoint) {
        String transactionId = ControllerLoggingAspect.getTransactionId();  // 트랜잭션 ID 가져오기
        String methodName = joinPoint.getSignature().toShortString();
        if (transactionId == null) {
            return;
        }
        logger.info("\n=== Repository End ===\n트랜잭션 ID: {}\nMethod: {}\n", transactionId, methodName);
    }
}
