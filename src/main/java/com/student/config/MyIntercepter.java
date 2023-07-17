package com.student.config;

import com.student.util.BaseContext;
import com.student.util.jwt.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 登录验证拦截器
 */
@Slf4j
public class MyIntercepter implements HandlerInterceptor {
//    private StringRedisTemplate stringRedisTemplate;

//    public MyIntercepter(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在处理器处理请求之前执行
        log.info("==========执行拦截器！！！========");
        String uri = request.getRequestURI();
        log.info("uri:"+ uri);
        if(uri.contains("login") || uri.contains("register")){
            return true;
        }else {
            if(request.getSession().getAttribute(JWTUtils.USER_LOGIN_TOKEN)!=null){
                //http的header中获得token
                //String token = request.getHeader(JWTUtils.USER_LOGIN_TOKEN);
                //String token = stringRedisTemplate.opsForValue().get(JWTUtils.USER_LOGIN_TOKEN);
                String token = (String) request.getSession().getAttribute(JWTUtils.USER_LOGIN_TOKEN);
                String userInformation = JWTUtils.validateToken(token);
                //存入本地线程
                BaseContext.setCurrentId(userInformation.split("-")[0]);
                //token不存在
                if (token == null || token.equals("")){
                    return false;
                }
                //验证token
                String sub = JWTUtils.validateToken(token);
                if (sub == null || sub.equals("")){
                    return false;
                }
                //更新token有效时间 (如果需要更新其实就是产生一个新的token)
                if (JWTUtils.isNeedUpdate(token)){
                    String newToken = JWTUtils.createToken(sub);
                    request.getSession().setAttribute(JWTUtils.USER_LOGIN_TOKEN,newToken);
                    //stringRedisTemplate.opsForValue().set(JWTUtils.USER_LOGIN_TOKEN, newToken,60, TimeUnit.MINUTES);
                }
                return true;
            }else {
//                String contextPath = request.getContextPath();
//                response.sendRedirect(contextPath + "/backend/login.html");
//                log.info("contextPath:"+contextPath);
            }
        }
        return true;
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

