package com.student.controller;

import com.student.Constant.RedisKey;
import com.student.annotaion.Log;
import com.student.config.SpringBeanUtils;
import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import com.student.pojo.userLogin;
import com.student.service.LoginService;
import com.student.util.BaseContext;
import com.student.util.CheckCodeUtil;
import com.student.util.jwt.JWTUtils;
import com.student.util.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
@Slf4j
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

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
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
    @Log(title="注册模块",action="register",params = true)
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
    @Log(title="用户名模块",action="getUserName",params = true)
    public String getUserName(HttpServletRequest req){
        //userDto user = (userDto) req.getSession().getAttribute("user");
        log.info("getUserName:{}",BaseContext.getCurrentId());
        String userName = BaseContext.getCurrentId();
        return userName;
    }

    @RequestMapping("/checkCode")
    @Log(title="验证码模块",action="checkCode",params = true)
    public void getCheckCode(HttpServletRequest req, HttpServletResponse res){
        res.setHeader("Pragma", "No-cache");
        res.setHeader("Cache-Control", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("image/jpeg");
        OutputStream fos = null;
        String checkCode = "";
        HttpSession session = req.getSession();
        try {
            fos = res.getOutputStream();
            checkCode = CheckCodeUtil.outputVerifyImage(100, 50, fos, 4);
/*            String redisCode = stringRedisTemplate.opsForValue().get(RedisKey.CODE_KEY);
            if (redisCode != null && !redisCode.equals("")) {
                Boolean delete = stringRedisTemplate.delete(RedisKey.CODE_KEY);
                if(Boolean.TRUE.equals(delete)){
                    stringRedisTemplate.opsForValue().set(RedisKey.CODE_KEY, checkCode, 300, TimeUnit.SECONDS);
                    log.info("checkCode:{}",checkCode);
                } else {
                    throw new RuntimeException("旧验证码删除失败");
                }
            }else {
                stringRedisTemplate.opsForValue().set(RedisKey.CODE_KEY, checkCode, 300, TimeUnit.SECONDS);
                log.info("checkCode:{}",checkCode);
            }*/
            if (session.getAttribute("code") != null) {
                session.removeAttribute("code");
                session.removeAttribute("codeTime");
                session.setAttribute("code", checkCode);
                session.setAttribute("codeTime", System.currentTimeMillis());
                log.info("checkCode:{}",checkCode);
            }else {
                session.setAttribute("code", checkCode);
                session.setAttribute("codeTime", System.currentTimeMillis());
                log.info("checkCode:{}",checkCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/emailCode/{email}")
    public Map<String,Object> email(@PathVariable("email") String email) {
        Map<String,Object> map = new HashMap<>();
        loginService.sendEmail(email);
        map.put("flag",true);
        return map;
    }

    @RequestMapping("/changePassword")
    public Map<String,Object> updateUser(@RequestBody userDto userDto){
        Map<String,Object> map = new HashMap<>();
        byte[] decode = Base64.getDecoder().decode(userDto.getPassword());
        String s = "";
        try {
            s = new String(decode, "utf-8");
            boolean b = loginService.change(s,userDto.getCheckCode());
            map.put("flag",b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping("/shutdown")
    @Log(title="关闭模块",action="shutdown")
    public void shutdown() {
        ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) SpringBeanUtils.getApplicationContext();
        log.info("----- 收到关闭服务请求，服务即将关闭！-----");
        applicationContext.close();
    }

}
