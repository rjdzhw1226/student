package com.student.mapper;

import com.student.pojo.student;
import com.student.pojo.subject;
import com.student.pojo.vo.subjectLabelVo;
import com.student.pojo.vo.subjectVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Mapper
public interface SubjectMapper {


    @Select("select s.*, t.name as teacherName from subject s, teacher t where t.id = s.teacher_id limit #{page},#{size}")
    public List<subjectVo> queryAll(Map<String, Object> params);

    @Select("SELECT s.id, s.name, concat(t1.label ,t2.label) AS gradeBetween, t3.name AS teacherName, s.count FROM subject s JOIN tree t1 ON s.grade_max = t1.id JOIN tree t2 ON s.grade_min = t2.id JOIN teacher t3 ON s.teacher_id = t3.id limit #{page},#{size}")
    public List<subject> queryAllLabel(Map<String, Object> params);

    @Select("select count(1) from subject where 1=1 ")
    public int queryCount();

    @Select("select tea.name from subject sub , teacher tea where sub.teacher_id = tea.id")
    public List<String> queryTeacherName();


    @Select("select count from subject where id = #{subId}")
    public int queryById(String subId);

    @Update("update subject set count = count - 1 where id = #{subId}")
    public int update(String subId);

    @Select("select * from subject where 1=1")
    public List<subject> queryAllReal();
}
