package com.student.controller;

import com.student.annotaion.Log;
import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import com.student.service.LoginService;
import com.student.util.BaseContext;
import com.student.util.CheckCodeUtil;
import com.student.util.jwt.JWTUtils;
import com.student.util.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LoginService loginService;

    @RequestMapping("/user")
    @Log(title="登录模块",action="login")
    public Map<String,Object> login(@RequestBody userDto user, HttpServletRequest req, HttpServletResponse res){
        Map<String,Object> map = new HashMap<>();
        if (loginService.login(user,req)) {
            String token = JWTUtils.createToken(user.getUsername().toString() + "-" + DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            req.getSession().setAttribute(JWTUtils.USER_LOGIN_TOKEN,token);
            map.put("flag",true);
            map.put("message","登录成功");
            map.put("data",user.getUsername());
            //stringRedisTemplate.opsForValue().set(JWTUtils.USER_LOGIN_TOKEN,(String) map.get("token"),60, TimeUnit.MINUTES);
            //res.setHeader(JWTUtils.USER_LOGIN_TOKEN, (String) map.get("token"));
            return map;
        }else {
            map.put("flag",false);
            map.put("message","登录失败");
            return map;
        }
    }

    @RequestMapping("/logout")
    @Log(title="登出模块",action="logout")
    public Map<String,Object> logout(HttpServletRequest req){
        Map<String,Object> map = new HashMap<>();
        try{
            String token =  (String) req.getSession().getAttribute(JWTUtils.USER_LOGIN_TOKEN);
            if(token != null && token != ""){
                req.getSession().removeAttribute(JWTUtils.USER_LOGIN_TOKEN);
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
    //TODO 改了session 拿不到暂时
    public String getUserName(HttpServletRequest req){
        //userDto user = (userDto) req.getSession().getAttribute("user");
        String userName = BaseContext.getCurrentId();
        return userName;
    }

    @RequestMapping("/checkCode")
    @Log(title="验证码模块",action="checkCode")
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
            if (req.getSession().getAttribute("code") != null) {
                req.getSession().removeAttribute("code");
                req.getSession().removeAttribute("codeTime");
                req.getSession().setAttribute("code", checkCode);
                req.getSession().setAttribute("codeTime", System.currentTimeMillis());
                System.out.println(checkCode);
            }else {
                req.getSession().setAttribute("code", checkCode);
                req.getSession().setAttribute("codeTime", System.currentTimeMillis());
                System.out.println(checkCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
