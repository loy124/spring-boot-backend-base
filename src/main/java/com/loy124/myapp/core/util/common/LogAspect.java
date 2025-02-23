package com.loy124.myapp.core.util.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Before("execution(* com.loy124.myapp.*.controller..*(..)) && " +
            "( @annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "  @annotation(org.springframework.web.bind.annotation.PatchMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.DeleteMapping) )")
    public void doLog(JoinPoint joinPoint) {

        //쓰레드 환경에서 UUID 관리
        String traceId = MDC.get("traceId");

        //traceId가 비어있는 경우에만 만들기
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().substring(0, 8);
            MDC.put("traceId", traceId);
        }

        //클래스.메서드
        String target = joinPoint.getSignature().toShortString();

        log.info("start [traceId]={} {} ", traceId, target);

    }

    @After("execution(* com.loy124.myapp.*.controller..*(..)) && " +
            "( @annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "  @annotation(org.springframework.web.bind.annotation.PatchMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.DeleteMapping) )")
    public void doEndLog(JoinPoint joinPoint) {
        String traceId = MDC.get("traceId");
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String target = joinPoint.getSignature().toShortString();
        log.info("end   [traceId]={} {}", traceId, target);
        if (targetClass.isAnnotationPresent(Controller.class)) {
            MDC.remove("traceId");
        }

    }


}
