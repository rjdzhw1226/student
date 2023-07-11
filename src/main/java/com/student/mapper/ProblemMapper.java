package com.student.mapper;

import com.student.pojo.item;
import com.student.pojo.problem;
import com.student.pojo.text;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface ProblemMapper {
    @Insert("INSERT INTO problem (id, time, name, middle_id) VALUES (#{id},#{time},#{name},#{middle_id})")
    public void saveProblem(String id, Date time, String name, String middle_id);
    @Insert("INSERT INTO item (id, title ,fatherItem_id,text_id ,type ,father_id ,sign) VALUES (#{id},#{title},#{fatherItem_id},#{text_id},#{type},#{father_id},#{sign})")
    public void saveItem(item it);

    @Insert("insert into middle (id, student_id) values (#{id},#{studentId})")
    public void saveConnect(String id, String studentId);

    @Insert("insert into text (id, Tikey, TiValue) values (#{id},#{Tikey},#{TiValue})")
    public void saveText(text text);
}
