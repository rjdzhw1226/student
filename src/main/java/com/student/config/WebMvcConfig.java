package com.student.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //可以不做映射 静态文件放resource下的static文件夹 动态文件放template文件夹
        log.info("项目静态资源映射启动！");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        registry.addInterceptor(new MyIntercepter())
                //拦截路径
                .addPathPatterns("/**")
                //放行路径
                .excludePathPatterns( //添加不拦截路径
                        "/login.html", //登录页面
                        "/register.html", //注册页面
                        "/login/**",       //登录请求
                        "/**/*.js",     //js静态资源
                        "/**/*.css",     //css静态资源
                        "/**/*.tff",
                        "/**/*.woff"
                );


    }

}
