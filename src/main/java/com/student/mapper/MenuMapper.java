package com.student.mapper;

import com.student.pojo.menuVo;
import com.student.pojo.menu_children;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper {

    @Select("select * from menu_children where id_connect = #{idc}")
    public List<menu_children> queryMenuChildren(String idc);

    @Select("select * from menu where 1=1")
    public List<menuVo> queryMenu();
}
