package com.student.mapper;

import com.student.pojo.classDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClassMapper {

    @Select("select * from class where id = #{id}")
    public classDto query(String id);
}
