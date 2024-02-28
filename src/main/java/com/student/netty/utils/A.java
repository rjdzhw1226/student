package com.student.netty.utils;

import com.student.service.LoginService;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class A {

	public static A a;

	@Resource
	public RedisCache redisService;

	@Resource
	public LoginService loginService;


	@PostConstruct
	public void init() {
		a = this;
		a.redisService = this.redisService;
		a.loginService = this.loginService;
	}
}
