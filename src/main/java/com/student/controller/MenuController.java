package com.student.controller;

import com.student.pojo.menu;
import com.student.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/query")
    @ResponseBody
    public Map<String,Object> queryMenu(){
        Map<String,Object> map = new HashMap<>();
        try{
            List<menu> menus = menuService.queryMenu();
            map.put("data",menus);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
