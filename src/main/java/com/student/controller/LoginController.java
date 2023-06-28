package com.student.controller;

import com.student.pojo.user;
import com.student.pojo.userDto;
import com.student.service.LoginService;
import com.student.util.CheckCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/user")
    public Map<String,Object> login(@RequestBody userDto user, HttpServletRequest req){
        Map<String,Object> map = new HashMap<>();
        if (loginService.login(user,req)) {
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

    @RequestMapping("/logout")
    public Map<String,Object> logout(HttpServletRequest req){
        Map<String,Object> map = new HashMap<>();
        try{
            userDto user = (userDto) req.getSession().getAttribute("user");
            if(user != null){
                req.getSession().removeAttribute("user");
                req.getSession().removeAttribute("code");
                req.getSession().removeAttribute("codeTime");
                map.put("code",1);
            }else{
                map.put("code",0);
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("code",e.getMessage());
        }
        return map;
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
        userDto user = (userDto) req.getSession().getAttribute("user");
        return user.getUsername();
    }

    @RequestMapping("/checkCode")
    public void getCheckCode(HttpServletRequest req, HttpServletResponse res){
        res.setHeader("Pragma", "No-cache");
        res.setHeader("Cache-Control", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("image/jpeg");
        OutputStream fos = null;
        String checkCode = "";
        try {
            fos = res.getOutputStream();
            checkCode = CheckCodeUtil.outputVerifyImage(100, 50, fos, 4);
            HttpSession session = req.getSession();
            if (session.getAttribute("code") != null) {
                session.removeAttribute("code");
                session.removeAttribute("codeTime");
                session.setAttribute("code", checkCode);
                session.setAttribute("codeTime", System.currentTimeMillis());
            }else {
                session.setAttribute("code", checkCode);
                session.setAttribute("codeTime", System.currentTimeMillis());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
