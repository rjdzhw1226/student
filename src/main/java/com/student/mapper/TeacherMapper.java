package com.student.mapper;

import com.student.pojo.dto.teacherDto;
import com.student.pojo.teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TeacherMapper {

    @Select("select name from teacher where id = #{id}")
    public String queryById(String id);

    @Select("select a.id,a.name,a.grade, t1.label as grade_class ,a.phone,a.position,a.status\n" +
            "from tree as t1,\n" +
            "     (select s.id as id, s.name as name, t.label as grade, s.class as class,s.phone as phone,s.position as position, s.status as status\n" +
            "      from tree as t,\n" +
            "           (select id ,name,class_id as grade, grade_id as class, phone,position,status from teacher where 1 = 1) as s\n" +
            "      where s.grade = t.id) as a\n" +
            "where t1.id = a.class\n" +
            "order by id\n" +
            "limit #{page},#{size}")
    public List<teacher> findAll(Map<String, Object> map);

    @Select("select class_id as grade, grade_id as grade_class from teacher where 1 = 1")
    public List<teacherDto> findAllTest(Map<String, Object> map);

    @Select("select count(1) from teacher where 1=1")
    public int count();
}
