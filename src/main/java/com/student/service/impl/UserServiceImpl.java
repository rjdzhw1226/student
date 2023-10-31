package com.student.service.impl;


import com.student.Constant.RedisKey;
import com.student.mapper.LoginMapper;
import com.student.pojo.user;
import com.student.pojo.userLogin;
import com.student.service.UserService;
import com.student.util.BaseContext;
import com.student.util.jwt.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;


@Service
public class UserServiceImpl implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private final LoginMapper userMapper;

    public UserServiceImpl(LoginMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user use = userMapper.query(username);
        if(use == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        BaseContext.setCurrentId(use.getUsername());
        //BaseContext.setCurrentId(getUserLogin().getUsername());
        //String token = JWTUtils.createToken(use.getId() + "-" + use.getUsername() + "-" + DigestUtils.md5DigestAsHex(use.getPassword().getBytes()));
        //stringRedisTemplate.opsForValue().set(RedisKey.USER_KEY + use.getId(), token);
        return new userLogin(use);
    }
}
