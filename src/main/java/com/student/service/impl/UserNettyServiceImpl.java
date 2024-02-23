package com.student.service.impl;


import com.student.mapper.UserMapper;
import com.student.pojo.vo.User;
import com.student.service.UserNettyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserNettyServiceImpl implements UserNettyService{

	@Resource
	private UserMapper userRespositoryDao;

	@Override
	public List<User> getFriendListData(String userId) {
		return userRespositoryDao.getFriendListById(userId);
	}

}
