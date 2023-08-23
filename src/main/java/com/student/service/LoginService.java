package com.student.service;

import com.student.mapper.LoginMapper;
import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

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
        System.out.println(DigestUtils.md5DigestAsHex("000000".getBytes()));
    }

    /**
     * <h3>mapper层</h3>
     */
    @Autowired
    private LoginMapper mapper;

    /**
     * <h2>登录服务</h2>
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
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        mapper.register(user);
    }
}
