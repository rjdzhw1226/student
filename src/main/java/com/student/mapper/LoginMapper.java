package com.student.mapper;

import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
