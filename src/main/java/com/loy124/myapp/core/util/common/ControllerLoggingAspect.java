package com.loy124.myapp.core.util.common;

import com.loy124.myapp.member.dto.LoginRequestDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

@Aspect
@Component
public class ControllerLoggingAspect {
    private static final ThreadLocal<String> transactionId = new ThreadLocal<>();
    private final Logger logger = LoggerFactory.getLogger("API_LOG");

    // 컨트롤러 메서드 시작 시점 로깅
    @Before("execution(* com.loy124.myapp..controller..*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        transactionId.set(UUID.randomUUID().toString());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURL().toString();

        String httpMethod = request.getMethod();

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp != null && !clientIp.isEmpty()) {
            // X-Forwarded-For 헤더에 여러 IP가 있는 경우 첫 번째 IP를 가져옴
            clientIp = clientIp.split(",")[0].trim();
        } else {
            // X-Forwarded-For 헤더가 없으면 REMOTE_ADDR을 사용
            clientIp = request.getRemoteAddr();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : "Anonymous";

        String methodName = joinPoint.getSignature().toShortString();


        if (methodName.contains("login")) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof LoginRequestDto) {
                LoginRequestDto loginRequest = (LoginRequestDto) args[0];
                username = loginRequest.getEmail();
            }
        }

        logger.info("\n=== Controller Start ===\n트랜잭션 ID: {}\nHTTP Method: {}\nAPI URL: {}\nClient IP: {}\nUser ID: {}\nMethod: {}\n",
                transactionId.get(), httpMethod, url, clientIp, username, methodName);
    }

    // 컨트롤러 메서드 반환 후 상태 코드에 따른 성공/실패 로깅
    @AfterReturning(pointcut = "execution(* com.loy124.myapp..controller..*(..))", returning = "result")
    public void logAfterReturningController(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();

        if (result instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) result;
            int statusCode = response.getStatusCodeValue();
            String outcome = (statusCode >= 200 && statusCode < 300) ? "성공" : "실패";
            logger.info("\n=== Controller End ===\n트랜잭션 ID: {}\nMethod: {}\n상태 코드: {}\n결과: {}\n",
                    transactionId.get(), methodName, statusCode, outcome);
        } else {
            logger.info("\n=== Controller End ===\n트랜잭션 ID: {}\nMethod: {}\n결과: 상태 코드 확인 불가\n",
                    transactionId.get(), methodName);
        }

        transactionId.remove();
    }

    // 트랜잭션 ID를 다른 클래스에서도 사용할 수 있도록 제공
    public static String getTransactionId() {
        return transactionId.get();
    }

    // 예외 발생 시 로깅
    @AfterThrowing(pointcut = "execution(* com.loy124.myapp..controller..*(..))", throwing = "exception")
    public void logAfterThrowingController(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("\n=== Controller Error ===\n트랜잭션 ID: {}\nMethod: {}\nError Message: {}\n",
                transactionId.get(), methodName, exception.getMessage());
        transactionId.remove();
    }
}
