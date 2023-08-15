package com.student.controller;

import com.student.mapper.TeacherMapper;
import com.student.pojo.DateSign;
import com.student.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pin")
public class SignController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping("/query/{username}")
    public DateSign All(@PathVariable("username") String username){
        teacherService.pinAll(username);
        return null;
    }
}
