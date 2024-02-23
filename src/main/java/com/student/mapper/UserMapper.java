package com.student.mapper;

import com.student.pojo.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserMapper {
    @Select("select b.id as userId, b.username as userName, b.gender as sex, b.image, b.username as showName from (select user_id, friend_id from friend where user_id = #{userId}) a left join user b on a.friend_id = b.id")
    List<User> getFriendListById(String userId);

}
