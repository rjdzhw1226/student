package com.student.service;

import com.student.mapper.MenuMapper;
import com.student.pojo.*;
import com.student.pojo.vo.menuVo;
import com.student.pojo.vo.treeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuMapper mapper;

    @Transactional
    public List<menu> queryMenu(){
        List<menu> menuResult = new ArrayList<>();
        List<menuVo> menuMain = mapper.queryMenu();
        for (menuVo menus : menuMain) {
            String idc = menus.getId();
            String name = menus.getName();
            List<menu_children> menuChildren = mapper.queryMenuChildren(idc);
            menu menu = new menu();
            menu.setId(idc);
            menu.setName(name);
            menu.setChildren(menuChildren);
            menuResult.add(menu);
        }
        return menuResult;
    }

    public List<treeVo> queryClassAll(String id){
        List<treeVo> tree = new ArrayList<>();
        List<treeVo> trees = mapper.queryClass(id);
        if(trees.size() > 0){
            tree.addAll(trees);
            for (treeVo treeVo : trees) {
                tree.addAll(queryClassAll(treeVo.getId()));
            }
        }
        return tree;
    }

    public List<tree> queryClassDiGui(String id){
        List<tree> tree = new ArrayList<>();
        List<treeVo> trees = mapper.queryClass(id);
        if(trees.size() > 0){
            for (treeVo treeVo : trees) {
                tree tr = new tree();
                tr.setId(treeVo.getId());
                tr.setLabel(treeVo.getLabel());
                tr.setChildren(queryClassDiGui(treeVo.getId()));
                tree.add(tr);
            }
        }
        return tree;
    }

}
