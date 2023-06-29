package com.student.service;

import com.student.mapper.LoginMapper;
import com.student.pojo.user;
import com.student.pojo.userDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class LoginService {
    @Autowired
    private LoginMapper mapper;
    public boolean login(userDto user, HttpServletRequest req) {
        String username = user.getUsername();
        String checkCode = user.getCheckCode();
        String code = (String) req.getSession().getAttribute("code");
        Long l2 = (Long) req.getSession().getAttribute("codeTime");
        long l1 = System.currentTimeMillis();
        if(checkCode.equals(code.toLowerCase()) && (l1-l2) < 300000){
            userDto login = mapper.login(username);
            if (login.getPassword().equals(user.getPassword())) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    public void register(user user) {
        mapper.register(user);
    }
}
