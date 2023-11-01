package com.student.service;

import com.student.Constant.RedisKey;
import com.student.mapper.LoginMapper;
import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import com.student.pojo.userLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 登录业务类<br>
 * <pre>
 *     登录服务{@link LoginService#login(userDto, HttpServletRequest)}<br>
 *     注册服务{@link LoginService#register(user)}
 * </pre>
 */
@Service
public class LoginService {
    public static void main(String[] args) {
        LoginService service = new LoginService();
        service.sendEmail("656779436@qq.com");
        System.out.println(DigestUtils.md5DigestAsHex("000000".getBytes()));
    }

    /**
     * <h3>mapper层</h3>
     */
    @Autowired
    private LoginMapper mapper;

    /**
     * <h3>邮件发送</h3>
     */
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * <h2>登录服务(拦截器)</h2>
     * <p>登录校验以及鉴权</p>
     * <pre>{@code
     *     登录参数传递
     *     String username = user.getUsername();
     *     String checkCode = user.getCheckCode();
     *     String password;
     * }
     * </pre>
     * @param user 用户名
     * @param req httpServlet请求
     * @return 是否登录成功
     * @since 1.8
     * @author RJD
     * @see userDto
     */
    public boolean login(userDto user, HttpServletRequest req) {
        String username = user.getUsername();
        String checkCode = user.getCheckCode();
        String code = (String) req.getSession().getAttribute("code");
        Long l2 = (Long) req.getSession().getAttribute("codeTime");
        long l1 = System.currentTimeMillis();
        if((checkCode.toLowerCase()).equals(code.toLowerCase()) && (l1-l2) < 300000){
            userDto login = mapper.login(username);
            if (login.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * <h2>注册服务</h2>
     * <p>注册用户并校验同名</p>
     * <pre>{@code
     *   注册参数传递
     *   String username = user.getUsername();
     *   String password;
     * }
     * </pre>
     * @param user 用户类
     * @since 1.8
     * @see user
     * @author RJD
     */
    public void register(user user) {
        String username = user.getUsername();
        if (mapper.registerCount(username) > 0) {
            throw new RuntimeException("已存在同名用户请重新注册");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        mapper.register(user);
    }

    public void sendEmail(String email) {
        userLogin user = getUserLogin();
        SimpleMailMessage message = new SimpleMailMessage();
        int intFlag = (int)(Math.random() * 100000000);
        stringRedisTemplate.opsForValue().set(RedisKey.CODE_KEY + user.getUsername(),String.valueOf(intFlag),5, TimeUnit.MINUTES);
        message.setTo(email);
        message.setText("验证码是：" + intFlag);
        message.setSentDate(new Date());
        //发送
        mailSender.send(message);
    }
    public boolean change(String pass, String checkCode) {
        userLogin user = getUserLogin();
        String unit = stringRedisTemplate.opsForValue().get(RedisKey.CODE_KEY + user.getUsername());
        if(unit.equals(checkCode)){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(pass);
            int i = mapper.updatePass(password, user.getUsername());
            if(i == 1){
                return true;
            }else {
                return false;
            }
        } else {
            return false;
        }
    }


    private static userLogin getUserLogin() {
        userLogin user = (userLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
