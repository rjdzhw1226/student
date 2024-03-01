package com.student.service;

import com.student.Constant.RedisKey;
import com.student.mapper.LoginMapper;
import com.student.netty.utils.RedisCache;
import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import com.student.pojo.userLogin;
import com.student.pojo.vo.MessageVo;
import com.student.pojo.vo.User;
import com.student.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.student.Constant.RedisKey.DOWNLINE_SIGN;
import static com.student.Constant.RedisKey.ONLINE_SIGN;

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

    @Resource
    private RedisCache redisCache;
    /**
     * <h3>mapper层</h3>
     */
    @Autowired
    private LoginMapper mapper;

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
        int intFlag = (int)(Math.random() * 100000000);
        stringRedisTemplate.opsForValue().set(RedisKey.CODE_KEY + user.getUsername(),String.valueOf(intFlag),5, TimeUnit.MINUTES);
        Mail mail = new Mail(email);
        try {
            //发送
            mail.ClientTestA(intFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    public user queryUser(String userName){
        return mapper.query(userName);
    }

    public User queryUserById(String userId){
        user user = mapper.queryId(userId);
        return new User(String.valueOf(user.getId()), user.getUsername(), user.getImage());
    }

    public List<String> queryUserByIds(List<String> userIds){
        return mapper.queryIds(userIds.toString().replace("[", ""));
    }

    public void saveGroupName(String groupId, List<String> userIds){
        for (String userId : userIds) {
            mapper.saveUserIds(groupId, userId);
        }
    }

    public List<String> findGroup(String groupId){
        return mapper.findGroup(groupId);
    }

    public void saveMessage(MessageVo vo){

    }

    /**
     * 聊天用户下线回调方法
     * @param userId
     */
    public void downLine(String userId) {
        try {
            //缓存下线时间
            redisCache.set(DOWNLINE_SIGN + "_" + userId, new Date(System.currentTimeMillis()));
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //删除用户在线标识
            redisCache.del(ONLINE_SIGN + "_" + userId);
        }


    }
}
