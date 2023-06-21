package com.student.mapper;

import com.student.pojo.student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Select("select * from student where 1=1")
    public List<student> queryAll();
}
