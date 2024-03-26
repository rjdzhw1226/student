package com.student.filter;

import com.student.exception.VerificationException;
import com.student.handler.MyFailureHandler;
import com.student.pojo.result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Slf4j
public class LoginTokenFilter extends OncePerRequestFilter {
    private MyFailureHandler failureHandler = new MyFailureHandler();
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        log.info("VerificationCodeFilter  doFilterInternal --- 执行");
        String uri = request.getRequestURI();
        if( !"/login".equals(uri)){
            //过滤器正常执行，不参与验证码操作
            filterChain.doFilter(request,response);
        } else {
            try{
                validCode(request);
                filterChain.doFilter(request,response);
            }catch (VerificationException e){
                e.printStackTrace();
                result result  = new result();
                result.setCode(500);
                result.setError(1002);
                result.setMsg("验证码错误！");
                failureHandler.setResult(result);
                failureHandler.onAuthenticationFailure(request,response,e);
            }
        }
    }

    private void validCode(HttpServletRequest request) {
        HttpSession session = request.getSession();
        //获取请求中的code
        String requestCode = request.getParameter("checkCode");
        //获取请求的code时间
        Long l2 = (Long) request.getSession().getAttribute("codeTime");
        //获取session中的code
        String sessionCode = "";

        Object attr = session.getAttribute("code");
        if(attr !=null ){
            sessionCode = (String)attr;
        }
        //处理逻辑
        if(!StringUtils.isEmpty(sessionCode)){
            session.removeAttribute("code");
        }

        //判断code是否正确。
        if( StringUtils.isEmpty(requestCode) || StringUtils.isEmpty(sessionCode) || !requestCode.toLowerCase().equals(sessionCode.toLowerCase()) ){
            //失败
            throw new RuntimeException();
        } else {
            long l1 = System.currentTimeMillis();
            if( l1 - l2 >= 300000){
                throw new RuntimeException();
            }
        }
    }
}
