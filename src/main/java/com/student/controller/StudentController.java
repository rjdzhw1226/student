package com.student.controller;

import com.student.pojo.student;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService service;

    @RequestMapping("/All")
    @ResponseBody
    public List<student> selectAll(){
        List<student> list = new ArrayList<>();
        List<student> students = service.queryAll();
        return students;
    }
}
