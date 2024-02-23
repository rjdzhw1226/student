package com.student.service;

import com.student.pojo.vo.User;

import java.util.List;

public interface UserNettyService {
     List<User> getFriendListData(String userId);
}
