package com.student;

import com.student.mapper.TeacherMapper;
import com.student.pojo.dto.teacherDtos;
import com.student.pojo.teacher;
import com.student.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class StudentDemoApplicationTests {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    public TeacherService ts;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void contextLoads() {
        Map<String, Object> map = new HashMap<>();
        map.put("page",0);
        map.put("size",10);
        List<teacherDtos> teacherList = teacherMapper.findAllTest1(map);
        List<teacher> collect = teacherList.stream().map(teacher -> {
            teacher teach = ts.teacherCommon(teacher);
            return teach;
        }).collect(Collectors.toList());
        log.info("collect:{}",collect);
    }

}
