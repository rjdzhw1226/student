package com.student.controller;

import com.student.pojo.problem;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @RequestMapping("/save")
    public Map<String,Object> save(@RequestBody problem pro){

        return null;
    }

}
