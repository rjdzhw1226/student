package com.student.service;

import com.student.mapper.LoginMapper;
import com.student.pojo.user;
import com.student.pojo.userDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private LoginMapper mapper;
    public boolean login(userDto user) {
        String username = user.getUsername();
        userDto login = mapper.login(username);
        if (login.getPassword().equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    public void register(user user) {
        mapper.register(user);
    }
}
