package com.student.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class MyIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在处理器处理请求之前执行
        System.out.println("==========执行拦截器！！========");
        String uri = request.getRequestURI();
        log.info("uri:"+uri);
        if(uri.contains("login") || uri.contains("register")){
            return true;
        }else {
            if(request.getSession().getAttribute("user")!=null){
                return true;
            }else {
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/backend/login.html");
                log.info("contextPath:"+contextPath);
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //在处理器处理请求完成后，返回ModelAndView之前执行。
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在DispatchServlet完全处理完请求后执行
    }
}

