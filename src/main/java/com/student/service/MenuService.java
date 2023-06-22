package com.student.service;

import com.student.mapper.MenuMapper;
import com.student.pojo.menu;
import com.student.pojo.menuVo;
import com.student.pojo.menu_children;
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
}
