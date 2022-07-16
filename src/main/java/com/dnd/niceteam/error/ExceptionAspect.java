package com.dnd.niceteam.error;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionAspect {

    @Before("execution(* com.dnd.niceteam.error.ApiExceptionHandler.*(..))")
    public void doExceptionLogBeforeHandler(JoinPoint joinPoint) {
        Exception ex = (Exception) joinPoint.getArgs()[0];
        log.error("{}", joinPoint.getSignature(), ex);
    }
}
