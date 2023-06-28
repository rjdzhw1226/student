package com.student.pojo;

import lombok.Data;

@Data
public class userDto {
    private String username; // 用户名，唯一
    private String password; // 密码
    private String checkCode; //验证码，五分钟有效
}
