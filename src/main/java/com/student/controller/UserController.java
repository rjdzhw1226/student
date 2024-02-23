package com.student.controller;


import com.student.pojo.dto.ResultDto;
import com.student.pojo.vo.User;
import com.student.service.UserNettyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wechat")
public class UserController {

	@Resource
	private UserNettyService userService;

    /**
     * 根据当前用户的userId 获取该用户的好友列表
     * @param userId
     * @return
     */
	@GetMapping("/friendList")
	public ResultDto getFriendList(String userId) {
		List<User> resultList = userService.getFriendListData(userId);
		return ResultDto.ok(resultList, "调用接口成功!");
	}
}
