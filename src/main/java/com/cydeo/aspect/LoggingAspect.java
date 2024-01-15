package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

//    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    //this method is to see who logged in
    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount userDetails = (SimpleKeycloakAccount) authentication.getDetails();
        return userDetails.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    @Pointcut("execution(* com.cydeo.controller.ProjectController.*(..)) || execution(* com.cydeo.controller.TaskController.*(..))")
    public void anyProjectAndTaskControllerPC() {
    }

    @Before("anyProjectAndTaskControllerPC()")
    public void beforeAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint) {
        log.info("Before -> Method: {}, User: {}"
                , joinPoint.getSignature().toShortString()
                , getUserName());
    }

    @AfterReturning(pointcut ="anyProjectAndTaskControllerPC()" , returning = "results")
    public void afterReturningAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint, Object results){
        log.info("AfterReturning -> Method: {}, User: {}, Results: {}"
                , joinPoint.getSignature().toShortString()
                , getUserName()
                , results.toString());
    }

    @AfterThrowing(pointcut = "anyProjectAndTaskControllerPC()" , throwing = "exception")
    public void afterThrowingAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint, Exception exception){
        log.info("AfterThrowing -> Method: {}, User: {}, Exception: {}"
                , joinPoint.getSignature().toShortString()
                , getUserName()
                , exception.getMessage());
    }
}

