package com.student.mapper;

import com.student.pojo.student;
import com.student.pojo.vo.subjectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Mapper
public interface SubjectMapper {


    @Select("select * from subject where 1=1 limit #{page},#{size}")
    public List<subjectVo> queryAll(Map<String, Object> params);

    @Select("select count(1) from subject where 1=1 ")
    public int queryCount();

    @Select("select tea.name from subject sub , teacher tea where sub.teacher_id = tea.id")
    public List<String> queryTeacherName();


}
