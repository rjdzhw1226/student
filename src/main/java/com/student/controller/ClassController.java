package com.student.controller;

import com.student.pojo.classVo;
import com.student.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @RequestMapping("/query/{id}")
    public classVo query(@PathVariable("id") String id){
        classVo queryResult = classService.query(id);
        return queryResult;
    }

    @RequestMapping("/findStudent")
    public Map<String,Object> findStudentByClass(){
        Map<String,Object> map = new HashMap<>();
        classService.findStudentByClass();
        return map;
    }
}
