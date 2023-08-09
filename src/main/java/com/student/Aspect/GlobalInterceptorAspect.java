package com.student.Aspect;

import com.student.annotaion.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class GlobalInterceptorAspect {

    @Pointcut("@annotation(com.student.annotaion.GlobalInterceptor)")
    public void GlobalAopPoint(){}

    @Before("GlobalAopPoint()")
    public void AspectWear(JoinPoint joinPoint){
        try {
            Object target = joinPoint.getTarget();
            Object[] arguments = joinPoint.getArgs();
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (interceptor.checkParams()) {
                for (int i = 0; i < arguments.length; i++) {
                    log.info("参数：{}",arguments[i].toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
