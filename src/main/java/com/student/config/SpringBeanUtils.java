package com.student.config;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtils {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext){
        SpringBeanUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}

