package com.student.controller;

import com.student.annotaion.Log;
import com.student.pojo.page;
import com.student.pojo.pageBean;
import com.student.pojo.subject;
import com.student.service.SubjectService;
import com.student.service.impl.SubjectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/subject")
@RestController
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @RequestMapping("/All")
    @ResponseBody
    public page<subject> query(@RequestBody pageBean page){
        page<subject> subjects = new page<>();
        Integer Page = page.getPage();
        Integer Size = page.getSize();
        try {
            //subjects = subjectService.queryAllSubject(Page, Size);
            subjects = subjectService.query(Page, Size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }

    @RequestMapping("/AllChoose")
    @ResponseBody
    public page<subject> queryChoose(@RequestBody pageBean page){
        page<subject> subjects = new page<>();
        Integer Page = page.getPage();
        Integer Size = page.getSize();
        try {
            subjects = subjectService.chooseSubject(Page, Size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }

    @RequestMapping("/doChoose/{subId}")
    @ResponseBody
    @Log(title="抢课模块",action="doChoose")
    public Map<String, Object> doChoose(@PathVariable("subId") String subId){
        //Map<String, Object> result = subjectService.connectSubject(subId);
        //新
        Map<String, Object> result = subjectService.doSubjectChoose(subId);
        return result;
    }
}
