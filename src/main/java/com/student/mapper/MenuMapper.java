package com.student.mapper;

import com.student.pojo.menuVo;
import com.student.pojo.menu_children;
import com.student.pojo.tree;
import com.student.pojo.treeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper {

    @Select("select * from menu_children where id_connect = #{idc}")
    public List<menu_children> queryMenuChildren(String idc);

    @Select("select * from menu where 1=1")
    public List<menuVo> queryMenu();

    @Select("select * from tree where id in(select id from tree where father_id= #{id})")
    public List<treeVo> queryClass(String id);

    @Select("select * from tree where level = #{level}")
    public List<treeVo> queryLevel(String level);

    @Select("select label from tree where id = #{id}")
    public String queryTreeById(String id);

    @Select("select label from tree where id = #{fatherId}")
    public String queryTreeTwoId(String fatherId);

    @Select("select id from tree where label = #{labelClass} and father_id = (select id from tree where label = #{label})")
    public String queryTreeTwoByLabel(String label, String labelClass);
}
