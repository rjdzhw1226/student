package com.student.controller;

import com.student.pojo.card;
import com.student.pojo.vo.classVo;
import com.student.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    private ClassService classService;
    @RequestMapping("/porn")
    public List<card> porn(){
        List<card> porn = classService.porn();
        return porn;
    }
}
