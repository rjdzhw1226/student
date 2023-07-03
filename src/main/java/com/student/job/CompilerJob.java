package com.student.job;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configurable
@EnableScheduling
public class CompilerJob {

    private static final Logger logger = LoggerFactory.getLogger(CompilerJob.class);

    private static boolean isExecute = false;

    /**
     * 任务：job test
     */
    @Scheduled(cron = "*/10 * * * * * ")
    public void test2() {
        try {
            if (isExecute) {
                return;
            }
            isExecute = true;		//只是测试，所以只执行一次
            System.out.println("定时任务");
        } catch (Exception e) {
            logger.error("test", e);
        }
    }



}
