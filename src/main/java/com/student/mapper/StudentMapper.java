package com.student.mapper;

import com.student.pojo.student;
import org.apache.ibatis.annotations.*;

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

    @Insert("insert into student (id, name, grade, grade_class, phone, age, gender, station,url) values (#{id},#{name},#{grade},#{grade_class},#{phone},#{age},#{gender},#{station},#{url})")
    public int add(student student);

    @Update("update student set id=#{studentId},name=#{name},grade=#{grade},grade_class=#{grade_class},phone=#{phone},age=#{age},gender=#{gender},url=#{url},station=#{station} where id=#{idEdit}")
    public int edit(String studentId,String name,String grade,String grade_class,String phone, String age, String gender,String station,String url, String idEdit);

    @Select("select  * from student where name like concat('%',#{queryStr},'%') or id like concat('%',#{queryStr},'%') or phone like concat('%',#{queryStr},'%') limit #{page},#{size}")
    public List<student> queryLike(String queryStr, Integer page, Integer size);
    @Select("select count(1) from student where name like concat('%',#{queryStr},'%') or id like concat('%',#{queryStr},'%') or phone like concat('%',#{queryStr},'%')")
    public int queryCountLike(String queryStr);

    @Delete("delete from student where id = #{id}")
    public int deleteId(String id);

    @Delete("delete from student where id in #{ids};")
    public int deleteIds(String ids);

    @Select("select  * from student where grade = #{grade} and grade_class = #{gradeClass}")
    public List<student> queryByGradeClass(String grade, String gradeClass);

    public void insertArticleTag(List<student> list);
}
