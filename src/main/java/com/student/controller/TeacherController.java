package com.student.controller;

import com.student.pojo.dto.studentDto;
import com.student.pojo.page;
import com.student.pojo.pageBean;
import com.student.pojo.student;
import com.student.pojo.teacher;
import com.student.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @RequestMapping("/queryAll")
    public page<teacher> All(@RequestBody pageBean page){
        page<teacher> teacherList = teacherService.findAll(page);
        return teacherList;
    }

    @RequestMapping("/add")
    public page<teacher> Add(){
        return null;
    }

    @RequestMapping("/querySub/{id}")
    public List<student> SubAll(@PathVariable("id") String id){
        return teacherService.querySub(id);
    }
}
