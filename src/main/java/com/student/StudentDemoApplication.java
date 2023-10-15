package com.student;

import com.student.util.BeanHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@SpringBootApplication
@ComponentScan(basePackages = {"com.student.**"})
@EnableAsync
@EnableTransactionManagement
@MapperScan("com.student.mapper")
//@ServletComponentScan
public class StudentDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(StudentDemoApplication.class, args);
//        BeanHolder beanHolder = new BeanHolder(run);
//        beanHolder.initSer();
        //SpringApplication.run(StudentDemoApplication.class, args);
    }

}
