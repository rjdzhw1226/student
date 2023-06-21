package com.student.controller;

import com.student.pojo.page;
import com.student.pojo.pageBean;
import com.student.pojo.statusBean;
import com.student.pojo.student;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService service;

    @RequestMapping("/All")
    @ResponseBody
    public page<student> selectAll(@RequestBody pageBean page){
        Integer Page = page.getPage();
        Integer Size = page.getSize();
        page<student> students = service.queryAll(Page, Size);
        return students;
    }

    @RequestMapping("/status")
    @ResponseBody
    public String statusChange(@RequestBody statusBean status){
        String id = status.getId();
        int i = service.updateStudent(status.getStation(),id);
        if(i>0){
            return "1";
        }else {
            return "0";
        }
    }
    @RequestMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestBody student student){
        int i = service.add(student);
        Map<String, Object> map = new HashMap<>();
        map.put("code",i);
        return map;
    }

    @RequestMapping("/edit/{id}")
    @ResponseBody
    public Map<String, Object> edit(@RequestBody student student , @PathVariable("id") String idEdit){
        Map<String, Object> map = new HashMap<>();
        int i = service.edit(student, idEdit);
        if(i>0){
            map.put("code",1);
        }else {
            map.put("code",0);
        }
        return map;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> queryById(@PathVariable("id") String id){
        Map<String, Object> map = new HashMap<>();
        try{
            student student = service.queryById(id);
            map.put("data",student);
            map.put("code",1);
        }catch (Exception e){
            e.printStackTrace();
            map.put("code",0);
        }
        return map;
    }

    @RequestMapping("/query/{queryStr}")
    @ResponseBody
    public page<student> queryStr(@PathVariable("queryStr") String queryStr, @RequestBody pageBean pageBean){
        Integer Page = pageBean.getPage();
        Integer Size = pageBean.getSize();
        page<student> studentpage = service.queryLike(queryStr, Page, Size);
        return studentpage;
    }

    @RequestMapping("/deleteIds")
    @ResponseBody
    public Map<String, Object> deleteIds( @RequestBody List<String> array){
        Map<String, Object> map = new HashMap<>();
        int i = service.deleteIds(array);
        if(i>0){
            map.put("code",1);
        }else {
            map.put("code",0);
        }
        return map;
    }

    @RequestMapping("/deleteId/{id}")
    @ResponseBody
    public Map<String, Object> deleteId( @PathVariable("id") String id){
        Map<String, Object> map = new HashMap<>();
        int i = service.deleteId(id);
        if(i>0){
            map.put("code",1);
        }else {
            map.put("code",0);
        }
        return map;
    }
}
