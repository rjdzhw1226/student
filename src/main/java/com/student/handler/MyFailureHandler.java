package com.student.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.pojo.result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class MyFailureHandler implements AuthenticationFailureHandler {

    private result result;

    public result getResult() {
        return result;
    }

    public void setResult(result result) {
        this.result = result;
    }

    /*
           参数：
             request : 请求对象
             response：应答对象
             authentication: spring security框架验证用户信息成功后的封装类。
         */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException {
        //当框架验证用户信息失败时执行的方法
        response.setContentType("text/json;charset=utf-8");

        if( result == null){
            result  localResult  = new result();
            localResult.setCode(1);
            localResult.setError(1001);
            localResult.setMsg("登录失败");
            result = localResult;
        }


        OutputStream out = response.getOutputStream();
        ObjectMapper om = new ObjectMapper();
        om.writeValue(out,result);
        out.flush();
        out.close();

    }
}
