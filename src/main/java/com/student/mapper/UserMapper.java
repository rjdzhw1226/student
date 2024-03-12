package com.student.mapper;

import com.student.pojo.Chat;
import com.student.pojo.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserMapper {
    @Select("select b.id as userId, b.username as userName, b.gender as sex, b.image, b.username as showName from (select user_id, friend_id from friend where user_id = #{userId}) a left join user b on a.friend_id = b.id")
    List<User> getFriendListById(String userId);

    @Select("select group_id as chatId, chat_name as chatName, chat_image as image from group_a where group_id in (select group_id from group_m where user_id = #{userId})")
    List<Chat> getChatList(String userId);

}
