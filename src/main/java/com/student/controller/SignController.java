package com.student.controller;

import com.student.mapper.TeacherMapper;
import com.student.pojo.DateSign;
import com.student.pojo.dto.DateSignDto;
import com.student.pojo.vo.DateSignVo;
import com.student.service.TeacherService;
import com.student.util.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pin")
public class SignController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping("/query")
    public List<DateSignVo> All(){
        String currentId = BaseContext.getCurrentId();
        List<DateSignVo> dateSignVos = teacherService.pinAll(currentId);
        return dateSignVos;
    }

    @RequestMapping("/sign")
    public Map<String, Object> save(@RequestBody DateSignDto dto){
        Map<String, Object> map = teacherService.savePin(dto);
        return map;
    }
}
