package com.student.controller;

import com.student.pojo.vo.classVo;
import com.student.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/class")
@Slf4j
public class ClassController {

    @Autowired
    private ClassService classService;

    @RequestMapping("/query/{id}")
    public classVo query(@PathVariable("id") String id){
        classVo queryResult = classService.query(id);
        return queryResult;
    }

    @RequestMapping("/findStudentChange/{name}")
    public Map<String,Object> findStudentByClassChange(@PathVariable("name") String name){
        Map<String,Object> map = new HashMap<>();
        //List<List<String>> studentByClassChange = classService.findStudentByClassString(name);
        classService.findStudentByClass(name);
        //map.put("data",studentByClassChange);
        return map;
    }

    @RequestMapping("/posts")
    @ResponseBody
    public Map<String,Object> posts(MultipartFile file){
        Map<String,Object> map = new HashMap<>();
        try {
            //
            //List<student> students = ExcelUtils.readMultipartFile(file, student.class);
            //log.info("数据：{}"+students);
            //classService.upDataGiveUp(students);
            classService.upData(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
