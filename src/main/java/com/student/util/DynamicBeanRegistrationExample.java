package com.student.util;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicBeanRegistrationExample {
    public static void main(String[] args) throws Exception {
        // 源代码
        String sourceCode = "public class HelloWorld { "
                + "    public static void main(String[] args) { "
                + "        System.out.println(\"Hello, dynamic compilation!\");"
                + "    } "
                + "} ";

        // 创建编译器对象
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // 创建内存中的源代码文件
        File sourceFile = new File("HelloWorld.java");
        java.nio.file.Files.write(sourceFile.toPath(), sourceCode.getBytes());

        // 执行编译，将源代码编译为字节码文件
        int compilationResult = compiler.run(null, null, null, sourceFile.getPath());

        if (compilationResult == 0) {
            // 创建URLClassLoader用于加载编译后的类
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{sourceFile.getParentFile().toURI().toURL()});

            // 加载动态编译后的类
            Class<?> compiledClass = Class.forName("HelloWorld", true, classLoader);
            String simpleName = compiledClass.getSimpleName();
            DyBeanRegister(compiledClass,simpleName);
            // 调用动态编译后的类的main方法
            //compiledClass.getMethod("main", String[].class).invoke(null, (Object) null);
        } else {
            System.out.println("Compilation Failed");
        }
    }

    public static <T> void DyBeanRegister(Class<T> clazz,String name) {
        // 创建ApplicationContext
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 获取Bean定义注册器
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();

        // 创建Bean定义
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(clazz);

        // 注册Bean定义
        registry.registerBeanDefinition(name, beanDefinition);

        // 启动应用上下文
        context.refresh();

        // 获取动态注册的Bean实例
        T bean = context.getBean(clazz);
        Class<?> aClass = bean.getClass();
        try {
            aClass.getMethod("main", String[].class).invoke(null, (Object) null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        // 关闭应用上下文
        context.close();
    }

    public static class MyDynamicBean {
        public void doSomething() {
            System.out.println("Dynamic bean is doing something...");
        }
    }

}

