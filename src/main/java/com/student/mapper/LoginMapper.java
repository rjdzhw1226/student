package com.student.mapper;

import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LoginMapper {
    @Select("select username, password from user where username = #{username}")
    public userDto login(String username);

    @Select("select id, username, password from user where username = #{username}")
    public user query(String username);

    @Insert("insert into user( id,birthday,gender,username,password,station,telephone) values(#{id},#{birthday},#{gender},#{username},#{password},#{station},#{telephone}) ")
    public void register(user user);

    @Select("select count(1) from user where username = #{username}")
    public int registerCount(String username);

    @Update("update user set password = #{password} where username = #{username}")
    public int updatePass(String password, String username);
    @Select("select * from user where id = #{userId}")
    public user queryId(String userId);

    @Insert("insert into group (group_id, user_id) values (#{groupId}, #{userId})))")
    public void saveUserIds(String groupId, String userId);
    @Select("select user_id from group where group_id = #{groupId}")
    List<String> findGroup(String groupId);
}
