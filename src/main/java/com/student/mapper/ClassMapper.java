package com.student.mapper;

import com.student.pojo.card;
import com.student.pojo.dto.classDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ClassMapper {

    @Select("select * from class where id = #{id}")
    public classDto query(String id);

    @Update("update class set count = #{count} where id = #{id}")
    public void editCount(Integer count, String id);
    @Select("select * from card where 1 = 1")
    public List<card> queryCard();
}
