package com.student.mapper;

import com.student.pojo.student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    @Select("select * from student where 1=1")
    public List<student> queryAll();

    @Select("select count(1) from student where 1=1")
    public int queryCount();

    @Select("select  * from student where 1=1 limit #{page},#{size}")
    public List<student> queryAllLimit(Map<String, Object> map);
    @Select("select * from student where id = #{id}")
    public student queryById(String id);

    @Update("update student set station=#{station} where id=#{id}")
    public int update(String station, String id);

    @Insert("insert into student (id, name, grade, grade_class, phone, age, gender, station) values (#{id},#{name},#{grade},#{grade_class},#{phone},#{age},#{gender},#{station})")
    public int add(student student);

    @Update("update student set id=#{studentId},name=#{name},grade=#{grade},grade_class=#{grade_class},phone=#{phone},age=#{age},gender=#{gender},station=#{station} where id=#{idEdit}")
    public int edit(String studentId,String name,String grade,String grade_class,String phone, String age, String gender,String station, String idEdit);

}
