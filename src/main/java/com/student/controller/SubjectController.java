package com.student.controller;

import com.student.pojo.page;
import com.student.pojo.subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/subject")
@RestController
public class SubjectController {

    @RequestMapping("/All")
    @ResponseBody
    public page<subject> query(){

        return null;
    }
}
