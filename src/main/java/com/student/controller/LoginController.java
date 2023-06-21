package com.student.controller;

import com.student.pojo.user;
import com.student.pojo.userDto;
import com.student.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/user")
    public Map<String,Object> login(@RequestBody userDto user, HttpServletRequest req){
        Map<String,Object> map = new HashMap<>();
        if (loginService.login(user)) {
            req.getSession().setAttribute("user",user);
            map.put("flag",true);
            map.put("message","登录成功");
            map.put("data",user.getUsername());
            return map;
        }else {
            map.put("flag",false);
            map.put("message","登录失败");
            return map;
        }
    }

    @RequestMapping("/register")
    public Map<String,Object> register(@RequestBody user user){
        Map<String,Object> map = new HashMap<>();
        try{
            //user.setId(Integer.parseInt(UUID.randomUUID().toString()));
            loginService.register(user);
            map.put("flag",true);
            map.put("message","注册成功");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("flag",false);
            map.put("message",e.getMessage());
            return map;
        }
    }

    @RequestMapping("/getUserName")
    public String getUserName(HttpServletRequest req){
        userDto user = (com.student.pojo.userDto) req.getSession().getAttribute("user");
        return user.getUsername();
    }

}
