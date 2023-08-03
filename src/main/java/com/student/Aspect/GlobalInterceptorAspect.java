package com.student.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GlobalInterceptorAspect {

    @Pointcut("@annotation(com.student.annotaion.GlobalInterceptor)")
    public void GlobalAopPoint(){}

    @Before("GlobalAopPoint()")
    public void AspectWear(){

    }

}
