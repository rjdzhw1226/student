package com.student.mapper;

import com.student.pojo.score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScoreMapper {

    @Select("select * from score where ownerId = #{id} and station = '1'")
    public List<score> queryScore(String id);

    @Select("select name from subject where id = #{subId}")
    public String querySubNameById(String subId);
}
