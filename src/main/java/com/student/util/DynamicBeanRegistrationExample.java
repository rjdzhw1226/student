package com.student.util;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DynamicBeanRegistrationExample {

    public static void main(String[] args) {
        // 创建ApplicationContext
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 获取Bean定义注册器
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();

        // 创建Bean定义
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(MyDynamicBean.class);

        // 注册Bean定义
        registry.registerBeanDefinition("dynamicBean", beanDefinition);

        // 启动应用上下文
        context.refresh();

        // 获取动态注册的Bean实例
        MyDynamicBean dynamicBean = context.getBean(MyDynamicBean.class);
        dynamicBean.doSomething();

        // 关闭应用上下文
        context.close();
    }

    public static class MyDynamicBean {
        public void doSomething() {
            System.out.println("Dynamic bean is doing something...");
        }
    }

}

