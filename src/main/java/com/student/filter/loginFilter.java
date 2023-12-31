package com.student.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登陆验证的过滤器
 */

@WebFilter("/*")
@Slf4j
public class loginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("------过滤器执行------");
        HttpServletRequest req = (HttpServletRequest) request;
        //判断用户访问资源路径是不是登录页面，是的话就放行
        String [] urls ={
                "/login.html",
                "/register.html",
                "/img/",
                "/login/user",
                "/login/register",
                "/login/logout",
                "/login/checkCode",
                "/file/**",
                "/backend/**",
                "/backend/plugins/**",
                "/front/**",};

        String s = req.getRequestURI().toString();

        for (String u:urls) {
            if (s.contains(u)) {
                //找到放行
                chain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = req.getSession();
        Object user = session.getAttribute("user");
        if (user != null){
            chain.doFilter(request, response);
        }else{
            req.setAttribute("login_msg","未登录，请登录");
            req.getRequestDispatcher("login.html").forward(req,response);
            response.getWriter().write(JSONObject.toJSONString("NOTLOGIN"));
        }
    }
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }


}
