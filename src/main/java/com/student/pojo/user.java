package com.student.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class user {
    private Long id; // 主键
    private Date birthday; // 生日
    private String gender; // 性别
    private String username; // 用户名，唯一
    private String password; // 密码
    private String station; // 状态
    private String telephone; // 联系电话
    private String image; //头像
}
